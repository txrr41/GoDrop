package com.project.shiphub.service.order;

import com.project.shiphub.dto.payment.CreatePaymentRequest;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderItem;
import com.project.shiphub.model.order.OrderStatus;
import com.project.shiphub.model.product.Product;
import com.project.shiphub.model.store.StoreProduct;
import com.project.shiphub.repository.order.OrderRepository;
import com.project.shiphub.repository.product.ProductRepository;
import com.project.shiphub.repository.store.DropperStoreRepository;
import com.project.shiphub.service.email.EmailServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DropperStoreRepository dropperStoreRepository;
    private final EmailServiceImp emailServiceImp;

    public Order createOrder(User user, BigDecimal totalAmount, CreatePaymentRequest request) {
        log.info("📦 Criando pedido para: {}", user.getEmail());

        Map<Long, BigDecimal> customPriceMap = buildCustomPriceMap(request.getStoreSlug());

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setBuyerName(request.getBuyerName());
        order.setBuyerEmail(request.getBuyerEmail());
        order.setBuyerPhone(request.getBuyerPhone());
        order.setBuyerCpf(request.getBuyerCpf());
        order.setShippingCep(request.getShippingCep());
        order.setShippingStreet(request.getShippingStreet());
        order.setShippingNumber(request.getShippingNumber());
        order.setShippingComplement(request.getShippingComplement());
        order.setShippingNeighborhood(request.getShippingNeighborhood());
        order.setShippingCity(request.getShippingCity());
        order.setShippingState(request.getShippingState());

        for (CreatePaymentRequest.CartItemDTO cartItem : request.getItems()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + cartItem.getProductId()));

            if (product.getEstoque() < cartItem.getQuantity()) {
                throw new RuntimeException("Estoque insuficiente: " + product.getNome());
            }

            BigDecimal unitCost = product.getPreco();

            BigDecimal customPrice = customPriceMap.getOrDefault(product.getId(), unitCost);

            BigDecimal totalItem = customPrice.multiply(new BigDecimal(cartItem.getQuantity()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(unitCost);
            orderItem.setCustomPrice(customPrice);
            orderItem.setTotalPrice(totalItem);

            order.getItems().add(orderItem);

            log.info("   ✓ {} x{} | custo GoDrop: R${} | preço dropper: R${} | margem dropper: R${}",
                    product.getNome(), cartItem.getQuantity(),
                    unitCost, customPrice,
                    customPrice.subtract(unitCost).multiply(new BigDecimal(cartItem.getQuantity())));
        }

        Order savedOrder = orderRepository.save(order);
        log.info("✅ Pedido #{} criado — cliente pagou R${}", savedOrder.getId(), totalAmount);
        return savedOrder;
    }

    private Map<Long, BigDecimal> buildCustomPriceMap(String storeSlug) {
        if (storeSlug == null || storeSlug.isBlank()) return Map.of();

        return dropperStoreRepository.findActiveBySlug(storeSlug)
                .map(store -> store.getStoreProducts().stream()
                        .collect(Collectors.toMap(
                                sp -> sp.getProduct().getId(),
                                StoreProduct::getCustomPrice
                        ))
                )
                .orElse(Map.of());
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public Order getOrderById(Long orderId, User user) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado");
        }
        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        OrderStatus current = order.getStatus();
        if (!isValidTransition(current, newStatus)) {
            throw new RuntimeException("Transição inválida: " + current + " → " + newStatus);
        }
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    private boolean isValidTransition(OrderStatus from, OrderStatus to) {
        return switch (from) {
            case PENDING          -> false;
            case PAYMENT_APPROVED -> to == OrderStatus.PROCESSING;
            case PAYMENT_FAILED   -> false;
            case PROCESSING       -> to == OrderStatus.SHIPPED;
            case SHIPPED, DELIVERED, CANCELLED, REFUNDED -> false;
        };
    }

    @Transactional
    public Order markAsShipped(Long orderId, String trackingCode) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Não é possível despachar pedido cancelado");
        }
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Pedido já foi despachado");
        }
        order.setStatus(OrderStatus.SHIPPED);
        order.setTrackingCode(trackingCode.trim().toUpperCase());
        Order shipped = orderRepository.save(order);
        emailServiceImp.sendTrackingEmail(shipped);
        return shipped;
    }

    public Map<String, Long> getOrderStatsByStatus() {
        return orderRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        o -> o.getStatus().toString(),
                        Collectors.counting()
                ));
    }

    public Long getTotalOrders() {
        return orderRepository.count();
    }
}
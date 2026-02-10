package com.project.shiphub.service.order;

import com.project.shiphub.dto.payment.CreatePaymentRequest;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderItem;
import com.project.shiphub.model.order.OrderStatus;
import com.project.shiphub.model.product.Product;
import com.project.shiphub.repository.order.OrderRepository;
import com.project.shiphub.repository.product.ProductRepository;
import com.project.shiphub.service.email.EmailServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final EmailServiceImp emailServiceImp;

    public Order createOrder(User user, BigDecimal totalAmount, CreatePaymentRequest request) {
        log.info("📦 Criando pedido para usuário: {}", user.getEmail());

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

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            log.info("🛒 Adicionando {} itens ao pedido", request.getItems().size());

            for (CreatePaymentRequest.CartItemDTO cartItem : request.getItems()) {
                Product product = productRepository.findById(cartItem.getProductId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + cartItem.getProductId()));

                if (product.getEstoque() < cartItem.getQuantity()) {
                    throw new RuntimeException("Estoque insuficiente para: " + product.getNome());
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setUnitPrice(product.getPreco());
                orderItem.setTotalPrice(product.getPreco().multiply(new BigDecimal(cartItem.getQuantity())));

                order.getItems().add(orderItem);

                log.info("   ✓ {} x{} - R$ {}", product.getNome(), cartItem.getQuantity(), orderItem.getTotalPrice());
            }
        } else {
            log.warn("⚠️ Nenhum item no carrinho!");
        }

        Order savedOrder = orderRepository.save(order);
        log.info("✅ Pedido #{} criado com sucesso!", savedOrder.getId());

        return savedOrder;
    }

    public List<Order> getUserOrders(User user) {
        log.info("📋 Buscando pedidos do usuário: {}", user.getEmail());

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        log.info("✅ Encontrados {} pedidos", orders.size());
        return orders;
    }

    public Order getOrderById(Long orderId, User user) {
        log.info("🔍 Buscando pedido #{} para usuário {}", orderId, user.getEmail());

        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (!order.getUser().getId().equals(user.getId())) {
            log.warn("⚠️ Tentativa de acesso não autorizado ao pedido #{}", orderId);
            throw new RuntimeException("Acesso negado");
        }

        return order;
    }

    public List<Order> getAllOrders() {
        log.info("👨‍💼 Buscando todos os pedidos do sistema");

        List<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc();

        log.info("✅ Encontrados {} pedidos no total", orders.size());
        return orders;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        log.info("🔄 Atualizando status do pedido #{} para {}", orderId, newStatus);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        OrderStatus currentStatus = order.getStatus();
        log.info("🔍 Status atual do pedido: {}", currentStatus);
        
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new RuntimeException(
                    "Transição inválida: " + currentStatus + " → " + newStatus
            );
        }

        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);

        log.info("✅ Status atualizado: {} → {}", currentStatus, newStatus);
        return updated;
    }

    private boolean isValidTransition(OrderStatus from, OrderStatus to) {
        return switch (from) {
            case PENDING -> false; // só webhook muda
            case PAYMENT_APPROVED -> to == OrderStatus.PROCESSING;
            case PAYMENT_FAILED -> false;
            case PROCESSING -> to == OrderStatus.SHIPPED;
            case SHIPPED, DELIVERED, CANCELLED -> false;
            case REFUNDED -> false;
        };
    }

    @Transactional
    public Order markAsShipped(Long orderId, String trackingCode) {
        log.info("📦 Despachando pedido #{} com rastreio {}", orderId, trackingCode);

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

        Order shippedOrder = orderRepository.save(order);

        log.info("✅ Pedido #{} despachado com sucesso", orderId);

        emailServiceImp.sendTrackingEmail(shippedOrder);

        return shippedOrder;
    }

    public Map<String, Long> getOrderStatsByStatus() {
        log.info("📊 Calculando estatísticas de pedidos");

        List<Order> allOrders = orderRepository.findAll();

        Map<String, Long> stats = allOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getStatus().toString(),
                        Collectors.counting()
                ));

        return stats;
    }

    public Long getTotalOrders() {
        return orderRepository.count();
    }



}
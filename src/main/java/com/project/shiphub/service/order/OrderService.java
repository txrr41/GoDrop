package com.project.shiphub.service.order;

import com.project.shiphub.dto.payment.CreatePaymentRequest;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderItem;
import com.project.shiphub.model.order.OrderStatus;
import com.project.shiphub.model.product.Product;
import com.project.shiphub.repository.order.OrderRepository;
import com.project.shiphub.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

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

        // ✅ Adicionar os itens do carrinho ao pedido
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            log.info("🛒 Adicionando {} itens ao pedido", request.getItems().size());

            for (CreatePaymentRequest.CartItemDTO cartItem : request.getItems()) {
                Product product = productRepository.findById(cartItem.getProductId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + cartItem.getProductId()));

                // Verificar estoque
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
}
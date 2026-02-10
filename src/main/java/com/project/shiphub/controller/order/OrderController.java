package com.project.shiphub.controller.order;

import com.project.shiphub.dto.order.OrderDTO;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.order.Order;
import com.project.shiphub.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getMyOrders(Authentication authentication) {

        if (authentication == null) {
            log.warn("⚠️ Tentativa de acesso sem autenticação");
            return ResponseEntity.status(401).build();
        }

        User user = (User) authentication.getPrincipal();
        log.info("📋 Buscando pedidos para: {}", user.getEmail());

        List<Order> orders = orderService.getUserOrders(user);

        List<OrderDTO> response = orders.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());

        log.info("✅ Retornando {} pedidos", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        User user = (User) authentication.getPrincipal();
        assert user != null;
        log.info("🔍 Buscando pedido #{} para {}", id, user.getEmail());

        try {
            Order order = orderService.getOrderById(id, user);
            OrderDTO response = new OrderDTO(order);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            log.error("❌ Erro ao buscar pedido: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
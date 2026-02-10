package com.project.shiphub.controller.admin;

import com.project.shiphub.dto.order.OrderDTO;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderStatus;
import com.project.shiphub.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Slf4j
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders(Authentication authentication) {

        if (authentication == null) {
            log.warn("⚠️ Tentativa de acesso admin sem autenticação");
            return ResponseEntity.status(401).build();
        }

        User user = (User) authentication.getPrincipal();
        assert user != null;
        log.info("👨‍💼 Admin {} acessando todos os pedidos", user.getEmail());

        List<Order> orders = orderService.getAllOrders();

        List<OrderDTO> response = orders.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());

        log.info("✅ Retornando {} pedidos para admin", response.size());
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        User admin = (User) authentication.getPrincipal();
        String newStatus = body.get("status");

        assert admin != null;
        log.info("👨‍💼 Admin {} alterando status do pedido #{} para {}",
                admin.getEmail(), id, newStatus);

        try {
            OrderStatus status = OrderStatus.valueOf(newStatus);

            Order order = orderService.updateOrderStatus(id, status);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Status atualizado com sucesso");
            response.put("order", new OrderDTO(order));

            log.info("✅ Status do pedido #{} atualizado para {}", id, newStatus);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.error("❌ Status inválido: {}", newStatus);
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Status inválido"));
        } catch (Exception e) {
            log.error("❌ Erro ao atualizar status: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }


    @PostMapping("/{id}/ship")
    public ResponseEntity<Map<String, Object>> shipOrder(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        User admin = (User) authentication.getPrincipal();
        String trackingCode = body.get("trackingCode");

        assert admin != null;
        log.info("📦 Admin {} despachando pedido #{} com rastreio {}",
                admin.getEmail(), id, trackingCode);

        try {
            if (trackingCode == null || trackingCode.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Código de rastreio obrigatório"));
            }

            Order order = orderService.markAsShipped(id, trackingCode);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pedido despachado com sucesso");
            response.put("order", new OrderDTO(order));

            log.info("✅ Pedido #{} despachado. Rastreio: {}", id, trackingCode);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ Erro ao despachar pedido: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getOrderStats(Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        log.info("📊 Buscando estatísticas de pedidos");

        Map<String, Long> statusCounts = orderService.getOrderStatsByStatus();

        Map<String, Object> response = new HashMap<>();
        response.put("totalOrders", orderService.getTotalOrders());
        response.put("byStatus", statusCounts);

        return ResponseEntity.ok(response);
    }
}
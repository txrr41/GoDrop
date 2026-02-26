package com.project.shiphub.controller.admin;

import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.order.Order;
import com.project.shiphub.repository.auth.LoginRepository;
import com.project.shiphub.repository.order.OrderRepository;
import com.project.shiphub.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@Slf4j
public class AdminDashboardController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final LoginRepository loginRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats(
            Authentication authentication
    ) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        User user = (User) authentication.getPrincipal();
        log.info("📊 Dashboard acessado por: {}", user.getEmail());

        // Pedidos por status
        var allOrders = orderRepository.findAll();

        long totalOrders = allOrders.size();
        long pendingOrders = allOrders.stream()
                .filter(o -> o.getStatus().name().equals("PENDING")).count();
        long approvedOrders = allOrders.stream()
                .filter(o -> o.getStatus().name().equals("PAYMENT_APPROVED")).count();
        long shippedOrders = allOrders.stream()
                .filter(o -> o.getStatus().name().equals("SHIPPED")).count();
        long deliveredOrders = allOrders.stream()
                .filter(o -> o.getStatus().name().equals("DELIVERED")).count();

        // Receita total (pedidos entregues)
        BigDecimal totalRevenue = allOrders.stream()
                .filter(o -> o.getStatus().name().equals("DELIVERED")
                        || o.getStatus().name().equals("PAYMENT_APPROVED")
                        || o.getStatus().name().equals("SHIPPED"))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var lowStockProducts = productRepository.findAll().stream()
                .filter(p -> p.getEstoque() != null && p.getEstoque() < 10)
                .map(p -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", p.getId());
                    item.put("nome", p.getNome());
                    item.put("estoque", p.getEstoque());
                    return item;
                })
                .toList();

        // Produtos e clientes
        long totalProducts = productRepository.count();
        long totalCustomers = loginRepository.count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", totalOrders);
        stats.put("totalRevenue", totalRevenue);
        stats.put("totalProducts", totalProducts);
        stats.put("totalCustomers", totalCustomers);
        stats.put("pendingOrders", pendingOrders);
        stats.put("approvedOrders", approvedOrders);
        stats.put("shippedOrders", shippedOrders);
        stats.put("deliveredOrders", deliveredOrders);
        stats.put("lowStockProducts", lowStockProducts);
        stats.put("lowStockCount", lowStockProducts.size());

        log.info("✅ Stats: {} pedidos, R$ {} receita", totalOrders, totalRevenue);
        return ResponseEntity.ok(stats);
    }
}
package com.project.shiphub.controller.admin;

import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderItem;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        int totalOrders = allOrders.size();
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

        // Novos clientes do mês
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        long newCustomers = loginRepository.findAll().stream()
                .filter(u -> u.getCreatedAt() != null && u.getCreatedAt().isAfter(startOfMonth))
                .count();

        //Receita mensal
        BigDecimal monthRevenue = allOrders.stream()
                .filter(o -> o.getCreatedAt().isAfter(startOfMonth))
                .filter(o -> o.getStatus().name().equals("DELIVERED")
                        || o.getStatus().name().equals("PAYMENT_APPROVED")
                        || o.getStatus().name().equals("SHIPPED"))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Ticket medio
        Double averageTicket = totalOrders > 0 ? monthRevenue.doubleValue() / totalOrders : 0.0;


        //Pedidos das ultimas 24hrs
        LocalDateTime lastHours = LocalDateTime.now().minusHours(24);
        List<Order> lastDayOrders = orderRepository.findByCreatedAtAfter(lastHours);
        List<Map<String, Object>> lastDayOrdersMapped = lastDayOrders.stream()
                .map(o -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", o.getId());
                    item.put("name", o.getBuyerName());
                    item.put("product", o.getItems().isEmpty() ? "—" : o.getItems().get(0).getProduct().getNome());
                    item.put("value", "R$ " + o.getTotalAmount());
                    item.put("status", o.getStatus().name());
                    item.put("statusKey", o.getStatus().name().toLowerCase());
                    item.put("initials", o.getBuyerName().substring(0, 1).toUpperCase());
                    item.put("bg", "#EEF2FF");
                    item.put("tc", "#6366F1");
                    return item;
                })
                .toList();

        var bestProducts = allOrders.stream()
                .filter(o -> o.getCreatedAt().isAfter(startOfMonth))
                .filter(o -> !o.getItems().isEmpty())
                .filter(o -> o.getStatus().name().equals("DELIVERED")
                        || o.getStatus().name().equals("PAYMENT_APPROVED")
                        || o.getStatus().name().equals("SHIPPED"))
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getProduct,
                Collectors.summingInt(OrderItem::getQuantity)
        ));

        var bestProductsList = bestProducts.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", entry.getKey().getNome());
                    item.put("category", entry.getKey().getCategoria());
                    item.put("units", entry.getValue());
                    item.put("bg", "#EEF2FF");
                    item.put("emoji", "📦");
                    item.put("pct", 100);
                    item.put("revenue", "R$ " + entry.getKey().getPreco());
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
        stats.put("newCustomers", newCustomers);
        stats.put("averageTicket", averageTicket);
        stats.put("monthRevenue", monthRevenue);
        stats.put("lastDayOrders", lastDayOrdersMapped);
        stats.put("bestProducts", bestProductsList);

        log.info("✅ Stats: {} pedidos, R$ {} receita", totalOrders, totalRevenue);
        return ResponseEntity.ok(stats);
    }
}
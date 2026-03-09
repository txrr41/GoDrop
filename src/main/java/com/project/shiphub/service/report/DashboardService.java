package com.project.shiphub.service.report;

import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderItem;
import com.project.shiphub.repository.auth.LoginRepository;
import com.project.shiphub.repository.order.OrderRepository;
import com.project.shiphub.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final LoginRepository loginRepository;

    public Map<String, Object> getStats() {
        var allOrders = orderRepository.findAll();
        var allProducts = productRepository.findAll();
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        int totalOrders = allOrders.size();
        long pendingOrders = countByStatus(allOrders, "PENDING");
        long approvedOrders = countByStatus(allOrders, "PAYMENT_APPROVED");
        long shippedOrders = countByStatus(allOrders, "SHIPPED");
        long deliveredOrders = countByStatus(allOrders, "DELIVERED");

        BigDecimal totalRevenue = sumRevenue(allOrders);
        BigDecimal monthRevenue = allOrders.stream()
                .filter(o -> o.getCreatedAt().isAfter(startOfMonth))
                .filter(o -> isPaid(o))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Double averageTicket = totalOrders > 0 ? monthRevenue.doubleValue() / totalOrders : 0.0;

        var lowStockProducts = allProducts.stream()
                .filter(p -> p.getEstoque() != null && p.getEstoque() < 10)
                .map(p -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", p.getId());
                    item.put("nome", p.getNome());
                    item.put("estoque", p.getEstoque());
                    item.put("categoria", p.getCategoria());
                    return item;
                }).toList();

        long newCustomers = loginRepository.findAll().stream()
                .filter(u -> u.getCreatedAt() != null && u.getCreatedAt().isAfter(startOfMonth))
                .count();

        LocalDateTime lastHours = LocalDateTime.now().minusHours(24);
        List<Order> lastDayOrders = orderRepository.findByCreatedAtAfter(lastHours);

        List<Map<String, Object>> lastDayOrdersMapped = lastDayOrders.stream()
                .map(o -> {

                    Map<String, Object> item = new HashMap<>();
                    item.put("id", o.getId());
                    item.put("name", o.getBuyerName());
                    item.put("product", o.getItems().isEmpty() ? "—" : o.getItems().get(0).getProduct().getNome());
                    item.put("value", "R$ " + o.getTotalAmount());
                    item.put("initials", o.getBuyerName().substring(0, 1).toUpperCase());
                    item.put("bg", "#EEF2FF");
                    item.put("tc", "#6366F1");

                    String statusKey = switch (o.getStatus().name()) {
                        case "PAYMENT_APPROVED" -> "approved";
                        case "SHIPPED" -> "shipped";
                        case "DELIVERED" -> "delivered";
                        case "PENDING" -> "pending";
                        default -> "processing";
                    };

                    item.put("statusKey", statusKey);
                    item.put("status", switch (statusKey) {
                        case "approved" -> "Aprovado";
                        case "shipped" -> "Enviado";
                        case "delivered" -> "Entregue";
                        case "pending" -> "Pendente";
                        default -> "Processando";
                    });
                    return item;
                }).toList();

        var bestProductsMap = allOrders.stream()
                .filter(o -> o.getCreatedAt().isAfter(startOfMonth))
                .filter(o -> !o.getItems().isEmpty())
                .filter(o -> isPaid(o))
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getProduct, Collectors.summingInt(OrderItem::getQuantity)));

        int maxUnits = bestProductsMap.values().stream().mapToInt(v -> v).max().orElse(1);
        var bestProductsList = bestProductsMap.entrySet().stream()
                .sorted(Map.Entry.<com.project.shiphub.model.product.Product, Integer>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", entry.getKey().getNome());
                    item.put("category", entry.getKey().getCategoria() != null ? entry.getKey().getCategoria() : "Sem categoria");
                    item.put("units", entry.getValue());
                    item.put("bg", "#EEF2FF");
                    item.put("emoji", "📦");
                    item.put("pct", (int) ((entry.getValue() * 100.0) / maxUnits));
                    item.put("revenue", "R$ " + entry.getKey().getPreco().multiply(BigDecimal.valueOf(entry.getValue())));
                    return item;
                }).toList();

        List<Map<String, Object>> revenueChart = buildRevenueChart(allOrders);

        Map<String, Long> categoryMap = allProducts.stream()
                .filter(p -> p.getCategoria() != null && p.getAtivo())
                .collect(Collectors.groupingBy(p -> p.getCategoria(), Collectors.counting()));

        long totalCategoryProducts = categoryMap.values().stream().mapToLong(v -> v).sum();
        String[] colors = {"#059669", "#6366F1", "#0EA5E9", "#F59E0B", "#EF4444"};
        int[] colorIdx = {0};
        List<Map<String, Object>> donutCategories = categoryMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(4)
                .map(entry -> {
                    Map<String, Object> cat = new HashMap<>();
                    cat.put("name", entry.getKey());
                    cat.put("pct", (int) (entry.getValue() * 100.0 / totalCategoryProducts));
                    cat.put("color", colors[colorIdx[0]++ % colors.length]);
                    return cat;
                }).toList();

        long totalDelivered = countByStatus(allOrders, "DELIVERED");
        long totalShipped = allOrders.stream().filter(o -> List.of("DELIVERED", "SHIPPED").contains(o.getStatus().name())).count();
        double onTimeDelivery = totalShipped > 0 ? (totalDelivered * 100.0 / totalShipped) : 0;
        long refundedOrders = countByStatus(allOrders, "CANCELLED");
        double refundRate = totalOrders > 0 ? (refundedOrders * 100.0 / totalOrders) : 0;

        List<Map<String, Object>> miniStats = List.of(
                Map.of("icon", "📦", "value", String.format("%.1f%%", onTimeDelivery), "label", "Entrega no prazo"),
                Map.of("icon", "↩️", "value", String.format("%.1f%%", refundRate), "label", "Taxa devolução"),
                Map.of("icon", "⭐", "value", "4.8", "label", "Avaliação média"),
                Map.of("icon", "⚡", "value", "1.2d", "label", "Tempo preparo")
        );

        String dateRange = startOfMonth.toLocalDate() + " — " + LocalDateTime.now().toLocalDate();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", totalOrders);
        stats.put("totalRevenue", totalRevenue);
        stats.put("totalProducts", productRepository.count());
        stats.put("totalCustomers", loginRepository.count());
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
        stats.put("revenueChart", revenueChart);
        stats.put("donutCategories", donutCategories);
        stats.put("miniStats", miniStats);
        stats.put("dateRange", dateRange);
        return stats;
    }

    private long countByStatus(List<Order> orders, String status) {
        return orders.stream().filter(o -> o.getStatus().name().equals(status)).count();
    }

    private boolean isPaid(Order o) {
        return List.of("DELIVERED", "PAYMENT_APPROVED", "SHIPPED").contains(o.getStatus().name());
    }

    private BigDecimal sumRevenue(List<Order> orders) {
        return orders.stream().filter(o -> isPaid(o)).map(Order::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<Map<String, Object>> buildRevenueChart(List<Order> allOrders) {
        String[] days = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};
        List<Map<String, Object>> chart = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDateTime dayStart = LocalDateTime.now().minusDays(i).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime dayEnd = LocalDateTime.now().minusDays(i).withHour(23).withMinute(59).withSecond(59);
            BigDecimal dayRevenue = allOrders.stream()
                    .filter(o -> o.getCreatedAt().isAfter(dayStart) && o.getCreatedAt().isBefore(dayEnd))
                    .filter(o -> isPaid(o))
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            Map<String, Object> day = new HashMap<>();
            day.put("day", days[dayStart.getDayOfWeek().getValue() % 7]);
            day.put("value", "R$ " + dayRevenue);
            day.put("amount", dayRevenue);
            day.put("highlight", i == 0);
            chart.add(day);
        }
        BigDecimal maxRevenue = chart.stream().map(d -> (BigDecimal) d.get("amount")).max(BigDecimal::compareTo).orElse(BigDecimal.ONE);
        chart.forEach(d -> {
            BigDecimal amount = (BigDecimal) d.get("amount");
            int pct = maxRevenue.compareTo(BigDecimal.ZERO) == 0 ? 0 :
                    amount.multiply(BigDecimal.valueOf(100)).divide(maxRevenue, 0, java.math.RoundingMode.HALF_UP).intValue();
            d.put("pct", pct);
        });
        return chart;
    }
}

package com.project.shiphub.dto.order;

import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDTO {

    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private String buyerName;
    private String buyerEmail;
    private String shippingAddress;
    private String trackingCode;
    private List<OrderItemDTO> items;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
        this.buyerName = order.getBuyerName();
        this.buyerEmail = order.getBuyerEmail();

        this.shippingAddress = String.format("%s, %s - %s/%s",
                order.getShippingStreet(),
                order.getShippingNumber(),
                order.getShippingCity(),
                order.getShippingState()
        );

        this.trackingCode = order.getTrackingCode();

        this.items = order.getItems().stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());
    }

    @Data
    public static class OrderItemDTO {
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;

        public OrderItemDTO(com.project.shiphub.model.order.OrderItem item) {
            this.productName = item.getProduct().getNome();
            this.quantity = item.getQuantity();
            this.unitPrice = item.getUnitPrice();
            this.totalPrice = item.getTotalPrice();
        }
    }
}
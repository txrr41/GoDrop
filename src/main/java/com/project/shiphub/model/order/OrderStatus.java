package com.project.shiphub.model.order;

public enum OrderStatus {
    PENDING,
    PAYMENT_APPROVED,
    PAYMENT_FAILED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REFUNDED
}
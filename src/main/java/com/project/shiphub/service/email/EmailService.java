package com.project.shiphub.service.email;

import com.project.shiphub.model.order.Order;
import java.time.LocalDateTime;

public interface EmailService {
    void sendOrderConfirmationEmail(Order order, LocalDateTime paymentHour);
    void sendTrackingEmail(Order order);
}
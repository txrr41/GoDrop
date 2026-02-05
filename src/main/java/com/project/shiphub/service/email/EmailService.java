package com.project.shiphub.service.email;

import com.stripe.model.PaymentLink;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface EmailService {
    void sendEmail(String to, String name, String product, BigDecimal totalAmount, String paymentMethod, LocalDateTime paymentHour);
}

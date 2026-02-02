package com.project.shiphub.dto.payment;

import com.project.shiphub.model.payment.Payment;
import com.project.shiphub.model.payment.PaymentStatus;
import lombok.Data;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Long id;
    private String stripePaymentId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private String clientSecret;

    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.stripePaymentId = payment.getStripePaymentId();
        this.amount = new BigDecimal(payment.getAmountInCents())
                .divide(new BigDecimal(100));
        this.currency = payment.getCurrency();
        this.status = payment.getStatus();
        this.paymentMethod = payment.getMethod().toString();
        this.createdAt = payment.getCreatedAt();
    }

    public PaymentResponse(Payment payment, String clientSecret) {
        this(payment);
        this.clientSecret = clientSecret;
    }
}

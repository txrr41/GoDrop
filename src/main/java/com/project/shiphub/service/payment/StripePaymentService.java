package com.project.shiphub.service.payment;

import com.project.shiphub.dto.payment.CreatePaymentRequest;
import com.project.shiphub.dto.payment.PaymentResponse;

public interface StripePaymentService {
    PaymentResponse createPayment(CreatePaymentRequest request);
    void confirmPayment(String paymentIntentId);
    void failPayment(String paymentIntentId, String errorMessage);
    PaymentResponse refundPayment(Long paymentId);
    PaymentResponse getPayment(Long paymentId);
    PaymentResponse getPaymentByStripeId(String stripePaymentId);
}

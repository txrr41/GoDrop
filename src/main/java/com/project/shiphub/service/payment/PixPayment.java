package com.project.shiphub.service.payment;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.resources.payment.Payment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PixPayment {
    public void createPixPayment() throws  Exception {
        PaymentCreateRequest request = PaymentCreateRequest.builder()
                .transactionAmount(new BigDecimal("100.00"))
                .paymentMethodId("pix")
                .payer(
                        PaymentPayerRequest.builder()
                                .email("teste@gmail.com")
                                .build()

                )
                .build();
        Payment payment = new PaymentClient().create(request);
        System.out.println("STATUS" + payment.getStatus());
    }
}

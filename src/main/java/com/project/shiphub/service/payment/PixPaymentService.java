package com.project.shiphub.service.payment;

import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.resources.payment.Payment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PixPaymentService {
    public void createPixPayment() {
        try {
            PaymentClient client = new PaymentClient();

            PaymentCreateRequest request =
                    PaymentCreateRequest.builder()
                            .transactionAmount(new BigDecimal("10.00"))
                            .description("Teste PIX Mercado Pago")
                            .paymentMethodId("pix")
                            .payer(
                                    PaymentPayerRequest.builder()
                                            .email("test_user_123@testuser.com")
                                            .firstName("Kauan")
                                            .lastName("Teste")
                                            .identification(
                                                    IdentificationRequest.builder()
                                                            .type("CPF")
                                                            .number("19119119100")
                                                            .build()
                                            )
                                            .build()
                            )
                            .build();

            Payment payment = client.create(request);

            System.out.println("PIX CRIADO COM SUCESSO");
            System.out.println("STATUS: " + payment.getStatus());
            System.out.println("QR CODE: " +
                    payment.getPointOfInteraction()
                            .getTransactionData()
                            .getQrCode());

        } catch (MPApiException e) {
            System.err.println("ERRO API MERCADO PAGO");
            System.err.println("STATUS CODE: " + e.getStatusCode());
            System.err.println("RESPONSE: " + e.getApiResponse().getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


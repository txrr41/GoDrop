package com.project.shiphub.service.payment;

import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PixPaymentService {

    public void createPixPayment() {
        try {
            System.out.println("🔄 Iniciando criação de pagamento PIX...");

            PaymentClient client = new PaymentClient();

            PaymentCreateRequest request = PaymentCreateRequest.builder()
                    .transactionAmount(new BigDecimal("100.00"))
                    .description("Teste PIX - Produto Exemplo")
                    .paymentMethodId("pix")
                    .payer(
                            PaymentPayerRequest.builder()
                                    .email("TESTUSER2263584545784725993@teste.com")  // ← Email padrão de teste
                                    .firstName("Test")
                                    .lastName("User")
                                    .identification(
                                            IdentificationRequest.builder()
                                                    .type("CPF")
                                                    .number("02648684045")  // ← CPF de teste válido
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            System.out.println("📤 Enviando requisição para Mercado Pago...");
            Payment payment = client.create(request);

            System.out.println("✅ PIX CRIADO COM SUCESSO");
            System.out.println("📋 ID: " + payment.getId());
            System.out.println("📋 STATUS: " + payment.getStatus());
            System.out.println("📋 QR CODE: " +
                    payment.getPointOfInteraction()
                            .getTransactionData()
                            .getQrCode());
            System.out.println("📋 QR CODE BASE64: " +
                    payment.getPointOfInteraction()
                            .getTransactionData()
                            .getQrCodeBase64());

        } catch (MPApiException e) {
            System.err.println("❌ ERRO API MERCADO PAGO");
            System.err.println("📋 STATUS CODE: " + e.getStatusCode());
            System.err.println("📋 RESPONSE: " + e.getApiResponse().getContent());
            System.err.println("📋 CAUSA: " + e.getMessage());
            e.printStackTrace();

        } catch (MPException e) {
            System.err.println("❌ ERRO MERCADO PAGO");
            System.err.println("📋 MENSAGEM: " + e.getMessage());
            e.printStackTrace();

        } catch (Exception e) {
            System.err.println("❌ ERRO GERAL");
            e.printStackTrace();
        }
    }
}
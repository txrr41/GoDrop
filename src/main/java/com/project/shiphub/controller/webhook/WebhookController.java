package com.project.shiphub.controller.webhook;

import com.project.shiphub.repository.order.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderStatus;
import com.project.shiphub.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final OrderRepository orderRepository;
    private final EmailService emailService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        // ✅✅✅ ADICIONE ESTAS LINHAS ✅✅✅
        System.out.println("\n\n");
        System.out.println("Webhook recebido");
        System.out.println("Tamanho do payload: " + payload.length() + " bytes");
        System.out.println("Signature: " + sigHeader.substring(0, Math.min(30, sigHeader.length())));
        System.out.println("\n");

        Event event;

        try {
            event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    webhookSecret
            );

            System.out.println("✅ Evento validado com sucesso!");
            System.out.println("Tipo: " + event.getType());

        } catch (SignatureVerificationException e) {
            System.out.println("❌ ERRO: Assinatura inválida!");
            System.out.println("Webhook secret configurado: " + (webhookSecret != null ? "SIM" : "NÃO"));
            return ResponseEntity.status(400).body("Invalid signature");
        }

        String eventType = event.getType();
        System.out.println("📬 Tipo de evento: " + eventType);

        try {
            switch (eventType) {
                case "payment_intent.succeeded":
                    System.out.println("🎉 PROCESSANDO PAGAMENTO APROVADO!");
                    handlePaymentSucceeded(event);
                    break;

                case "payment_intent.payment_failed":
                    System.out.println("❌ PROCESSANDO PAGAMENTO FALHO");
                    handlePaymentFailed(event);
                    break;

                default:
                    System.out.println("ℹ️ Evento não processado: " + eventType);
            }

            System.out.println("✅ Webhook processado com sucesso!\n\n");
            return ResponseEntity.ok("Webhook processed successfully");

        } catch (Exception e) {
            System.out.println("❌ ERRO ao processar webhook:");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Webhook processing failed");
        }
    }

    private void handlePaymentSucceeded(Event event) {
        try {
            PaymentIntent paymentIntent = (PaymentIntent) event
                    .getDataObjectDeserializer()
                    .deserializeUnsafe();


            System.out.println("PaymentIntent ID: " + paymentIntent.getId());
            System.out.println("Valor: R$ " + (paymentIntent.getAmountReceived() / 100.0));

            System.out.println("\n📦 METADADOS RECEBIDOS:");
            if (paymentIntent.getMetadata() != null && !paymentIntent.getMetadata().isEmpty()) {
                paymentIntent.getMetadata().forEach((key, value) ->
                        System.out.println("   " + key + " = " + value)
                );
            } else {
                System.out.println("   ⚠️ NENHUM METADATA!");
            }

            String orderIdStr = paymentIntent.getMetadata().get("order_id");

            if (orderIdStr == null || orderIdStr.isEmpty()) {
                System.out.println("❌ ERRO: order_id NÃO ENCONTRADO!");
                System.out.println("Metadata disponível: " + paymentIntent.getMetadata());
                return;
            }

            System.out.println("✅ order_id encontrado: " + orderIdStr);

            Long orderId = Long.parseLong(orderIdStr);
            System.out.println("Buscando pedido #" + orderId + " no banco...");

            Order order = orderRepository.findByIdWithItems(orderId)
                    .orElseThrow(() -> {
                        System.out.println("❌ Pedido #" + orderId + " NÃO ENCONTRADO no banco!");
                        return new RuntimeException("Pedido não encontrado");
                    });

            System.out.println("✅ Pedido encontrado!");
            System.out.println("   ID: " + order.getId());
            System.out.println("   Status ATUAL: " + order.getStatus());
            System.out.println("   Comprador: " + order.getBuyerEmail());

            System.out.println("\n🔄 Atualizando status...");
            OrderStatus statusAnterior = order.getStatus();
            order.setStatus(OrderStatus.PAYMENT_APPROVED);

            Order saved = orderRepository.save(order);

            System.out.println("✅ Status atualizado!");
            System.out.println("   Antes: " + statusAnterior);
            System.out.println("   Depois: " + saved.getStatus());


            Order verificacao = orderRepository.findById(orderId).orElse(null);
            if (verificacao != null) {
                System.out.println("\n🔍 Verificação no banco:");
                System.out.println("   Status: " + verificacao.getStatus());

                if (verificacao.getStatus() != OrderStatus.PAYMENT_APPROVED) {
                    System.out.println("❌❌❌ ERRO: Status NÃO salvou!");
                }
            }

            System.out.println("\n📧 Enviando email...");
            try {
                emailService.sendOrderConfirmationEmail(order, LocalDateTime.now());
                System.out.println("✅ Email enviado!");
            } catch (Exception e) {
                System.out.println("⚠️ Erro ao enviar email: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("❌ ERRO em handlePaymentSucceeded:");
            e.printStackTrace();
            throw new RuntimeException("Erro ao processar pagamento", e);
        }
    }

    private void handlePaymentFailed(Event event) {
        try {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow(() -> new RuntimeException("Failed to deserialize PaymentIntent"));

            String errorMessage = paymentIntent.getLastPaymentError() != null
                    ? paymentIntent.getLastPaymentError().getMessage()
                    : "Erro desconhecido";

            System.out.println("❌ Pagamento falhou: " + errorMessage);

            String orderIdStr = paymentIntent.getMetadata().get("order_id");
            if (orderIdStr == null) return;

            Long orderId = Long.parseLong(orderIdStr);
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

            order.setStatus(OrderStatus.PAYMENT_FAILED);
            orderRepository.save(order);

            System.out.println("Pedido #" + orderId + " marcado como PAYMENT_FAILED");

        } catch (Exception e) {
            System.out.println("❌ Erro em handlePaymentFailed:");
            e.printStackTrace();
        }
    }
}
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
        log.info("🔔 Webhook recebido do Stripe");

        Event event;

        try {
            event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    webhookSecret
            );

        } catch (SignatureVerificationException e) {
            log.error("❌ Assinatura inválida! Requisição rejeitada.");
            return ResponseEntity.status(400).body("Invalid signature");
        }

        String eventType = event.getType();
        log.info("📬 Tipo de evento: {}", eventType);

        try {
            switch (eventType) {

                // PAGAMENTO APROVADO (PaymentIntent succeeded)
                case "payment_intent.succeeded":
                    handlePaymentSucceeded(event);
                    break;

                // PAGAMENTO FALHOU
                case "payment_intent.payment_failed":
                    log.warn("❌ Pagamento falhou");
                    handlePaymentFailed(event);
                    break;

                // REEMBOLSO
                case "charge.refunded":
                    log.info("🔄 Reembolso processado");
                    handleRefund(event);
                    break;

                default:
                    log.info("ℹ️ Evento não processado: {}", eventType);
            }

            return ResponseEntity.ok("Webhook processed successfully");

        } catch (Exception e) {
            log.error("❌ Erro ao processar webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Webhook processing failed");
        }
    }

    private void handlePaymentSucceeded(Event event) {
        try {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow(() -> new RuntimeException("Failed to deserialize PaymentIntent"));

            log.info("🎉 Pagamento aprovado!");
            log.info("   PaymentIntent ID: {}", paymentIntent.getId());
            log.info("   Valor recebido: R$ {}", paymentIntent.getAmountReceived() / 100.0);

            String orderIdStr = paymentIntent.getMetadata().get("order_id");

            if (orderIdStr == null || orderIdStr.isEmpty()) {
                log.error("❌ orderId não encontrado nos metadata!");
                log.error("   Metadata disponível: {}", paymentIntent.getMetadata());
                return;
            }

            Long orderId = Long.parseLong(orderIdStr);
            log.info("📦 Pedido encontrado: #{}", orderId);

            // Buscar pedido com itens
            Order order = orderRepository.findByIdWithItems(orderId)
                    .orElseThrow(() -> new RuntimeException("Pedido #" + orderId + " não encontrado"));

            // Atualizar status do pedido
            order.setStatus(OrderStatus.PAYMENT_APPROVED);
            orderRepository.save(order);

            log.info("✅ Pedido #{} atualizado para PAYMENT_APPROVED", orderId);

            // Enviar email de confirmação
            try {
                emailService.sendOrderConfirmationEmail(order, LocalDateTime.now());
                log.info("📧 Email de confirmação enviado para {}", order.getBuyerEmail());
            } catch (Exception e) {
                log.error("⚠️ Erro ao enviar email: {}", e.getMessage());
            }

        } catch (Exception e) {
            log.error("❌ Erro ao processar payment_intent.succeeded: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao processar pagamento aprovado", e);
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

            log.error("❌ Pagamento falhou!");
            log.error("   PaymentIntent ID: {}", paymentIntent.getId());
            log.error("   Motivo: {}", errorMessage);

            String orderIdStr = paymentIntent.getMetadata().get("order_id");
            if (orderIdStr == null) return;

            Long orderId = Long.parseLong(orderIdStr);
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

            order.setStatus(OrderStatus.PAYMENT_FAILED);
            orderRepository.save(order);

            log.info("💾 Pedido #{} marcado como PAYMENT_FAILED", orderId);

        } catch (Exception e) {
            log.error("❌ Erro ao processar payment_intent.payment_failed: {}", e.getMessage());
        }
    }

    private void handleRefund(Event event) {
        try {
            log.info("🔄 Reembolso processado - implementar lógica se necessário");
            // Implementar lógica de reembolso
        } catch (Exception e) {
            log.error("❌ Erro ao processar reembolso: {}", e.getMessage());
        }
    }
}
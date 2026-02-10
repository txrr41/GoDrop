package com.project.shiphub.controller.webhook;

import com.project.shiphub.repository.order.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
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

        switch (eventType) {

            // PAGAMENTO APROVADO )
            case "checkout.session.completed":
                handleCheckoutCompleted(event);
                break;

            // PAGAMENTO RECEBIDO
            case "payment_intent.succeeded":
                log.info("💰 Pagamento recebido com sucesso");
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
    }

    private void handleCheckoutCompleted(Event event) {
        try {
            Session session = (Session) event.getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow(() -> new RuntimeException("Failed to deserialize event"));

            log.info("🎉 Checkout completado!");
            log.info("   Session ID: {}", session.getId());
            log.info("   Payment Status: {}", session.getPaymentStatus());
            log.info("   Customer Email: {}", session.getCustomerDetails().getEmail());
            String orderIdStr = session.getMetadata().get("orderId");

            if (orderIdStr == null || orderIdStr.isEmpty()) {
                log.error("❌ orderId não encontrado nos metadata!");
                return;
            }

            Long orderId = Long.parseLong(orderIdStr);
            log.info("📦 Pedido encontrado: #{}", orderId);

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Pedido #" + orderId + " não encontrado"));

            order.setStatus(OrderStatus.PAYMENT_APPROVED);
            orderRepository.save(order);

            log.info("✅ Pedido #{} atualizado para PAYMENT_APPROVED", orderId);

            try {
                emailService.sendOrderConfirmationEmail(order, LocalDateTime.now());
                log.info("📧 Email de confirmação enviado");
            } catch (Exception e) {
                log.error("⚠️ Erro ao enviar email: {}", e.getMessage());
            }

        } catch (Exception e) {
            log.error("❌ Erro ao processar checkout completed: {}", e.getMessage(), e);
        }
    }

    private void handlePaymentFailed(Event event) {
        try {
            log.warn("⚠️ Pagamento falhou - implementar lógica se necessário");

        } catch (Exception e) {
            log.error("❌ Erro ao processar pagamento falho: {}", e.getMessage());
        }
    }

    private void handleRefund(Event event) {
        try {
            log.info("🔄 Reembolso processado - implementar lógica se necessário");

        } catch (Exception e) {
            log.error("❌ Erro ao processar reembolso: {}", e.getMessage());
        }
    }
}
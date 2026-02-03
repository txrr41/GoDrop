package com.project.shiphub.controller.webhook;

import com.project.shiphub.service.webhook.WebhookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {

        log.info("📨 Webhook recebido do Stripe");
        log.debug("   Payload length: {} bytes", payload.length());
        log.debug("   Signature: {}...", sigHeader.substring(0, 20));

        Event event;

        try {
            event = webhookService.constructEventFromPayload(payload, sigHeader);
            log.info("✅ Assinatura validada");

        } catch (SignatureVerificationException e) {
            log.error("⚠️ Webhook com assinatura inválida: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid signature");
        }

        try {
            webhookService.processEvent(event);
            log.info("✅ Evento processado: {}", event.getType());
            return ResponseEntity.ok("Webhook processed");

        } catch (Exception e) {
            log.error("❌ Erro ao processar webhook: {}", e.getMessage(), e);

            return ResponseEntity.ok("Error logged");
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testWebhook() {
        log.info("✅ Endpoint de webhook está funcionando!");
        return ResponseEntity.ok("Webhook endpoint is working!");
    }
}

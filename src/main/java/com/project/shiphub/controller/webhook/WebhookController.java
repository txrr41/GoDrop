package com.project.shiphub.controller.webhook;

import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderItem;
import com.project.shiphub.model.order.OrderStatus;
import com.project.shiphub.model.product.Product;
import com.project.shiphub.repository.order.OrderRepository;
import com.project.shiphub.repository.product.ProductRepository;
import com.project.shiphub.service.auth.DropperService;
import com.project.shiphub.service.email.EmailService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
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
    private final ProductRepository productRepository;
    private final DropperService dropperService;
    private final EmailService emailService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            log.info("✅ Webhook recebido: {}", event.getType());
        } catch (SignatureVerificationException e) {
            log.error("❌ Assinatura inválida");
            return ResponseEntity.status(400).body("Invalid signature");
        }

        try {
            switch (event.getType()) {
                case "payment_intent.succeeded"      -> handlePaymentSucceeded(event);
                case "payment_intent.payment_failed" -> handlePaymentFailed(event);
                default -> log.info("ℹ️ Evento ignorado: {}", event.getType());
            }
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            log.error("❌ Erro ao processar webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Webhook processing failed");
        }
    }

    private void handlePaymentSucceeded(Event event) throws StripeException {
        // Busca o PaymentIntent diretamente na Stripe pelo ID do evento
        // Evita problemas de incompatibilidade de versão na deserialização
        String paymentIntentId = event.getDataObjectDeserializer()
                .getRawJson()
                .contains("\"id\"") ? extractIdFromEvent(event) : null;

        if (paymentIntentId == null) {
            log.error("❌ Não foi possível extrair o PaymentIntent ID do evento");
            return;
        }

        log.info("🔍 Buscando PaymentIntent na Stripe: {}", paymentIntentId);
        PaymentIntent pi = PaymentIntent.retrieve(paymentIntentId);

        String orderIdStr = pi.getMetadata().get("order_id");
        if (orderIdStr == null) {
            log.error("❌ order_id não encontrado nos metadados do PaymentIntent {}", paymentIntentId);
            return;
        }

        Long orderId = Long.parseLong(orderIdStr);
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido #" + orderId + " não encontrado"));

        if (order.getStatus() == OrderStatus.PAYMENT_APPROVED) {
            log.warn("⚠️ Pedido #{} já aprovado — ignorando duplicata", orderId);
            return;
        }

        // ✅ Desconta estoque de cada item do pedido
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            int estoqueAtual = product.getEstoque() != null ? product.getEstoque() : 0;
            int novoEstoque  = Math.max(0, estoqueAtual - item.getQuantity());
            product.setEstoque(novoEstoque);
            productRepository.save(product);
            log.info("📦 Estoque: {} | {} → {}", product.getNome(), estoqueAtual, novoEstoque);
        }

        order.setStatus(OrderStatus.PAYMENT_APPROVED);
        orderRepository.save(order);
        log.info("✅ Pedido #{} aprovado e estoque atualizado", orderId);

        try {
            emailService.sendOrderConfirmationEmail(order, LocalDateTime.now());
        } catch (Exception e) {
            log.warn("⚠️ Erro ao enviar email: {}", e.getMessage());
        }

        try {
            dropperService.updateSalesAndLevel(order.getUser().getId(), order.getTotalAmount());
        } catch (Exception e) {
            log.debug("Usuário não é dropper: {}", e.getMessage());
        }
    }

    private void handlePaymentFailed(Event event) {
        String paymentIntentId = extractIdFromEvent(event);
        if (paymentIntentId == null) return;

        try {
            PaymentIntent pi = PaymentIntent.retrieve(paymentIntentId);
            String orderIdStr = pi.getMetadata().get("order_id");
            if (orderIdStr == null) return;

            orderRepository.findById(Long.parseLong(orderIdStr)).ifPresent(order -> {
                order.setStatus(OrderStatus.PAYMENT_FAILED);
                orderRepository.save(order);
                log.info("❌ Pedido #{} marcado como PAYMENT_FAILED", order.getId());
            });
        } catch (StripeException e) {
            log.error("❌ Erro ao buscar PaymentIntent: {}", e.getMessage());
        }
    }

    /**
     * Extrai o "id" do objeto de dados do evento diretamente do JSON bruto.
     * Mais robusto que deserializar, funciona independente da versão da lib.
     */
    private String extractIdFromEvent(Event event) {
        try {
            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            String raw = deserializer.getRawJson();
            // O JSON tem formato: {"id":"pi_xxx", ...}
            int idx = raw.indexOf("\"id\":\"");
            if (idx == -1) return null;
            int start = idx + 6;
            int end   = raw.indexOf("\"", start);
            return raw.substring(start, end);
        } catch (Exception e) {
            log.error("❌ Erro ao extrair ID do evento: {}", e.getMessage());
            return null;
        }
    }
}
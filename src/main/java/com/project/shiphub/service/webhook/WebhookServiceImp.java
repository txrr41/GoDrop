package com.project.shiphub.service.webhook;

import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderStatus;
import com.project.shiphub.model.payment.Payment;
import com.project.shiphub.model.payment.PaymentStatus;
import com.project.shiphub.repository.order.OrderRepository;
import com.project.shiphub.repository.payment.PaymentRepository;
import com.project.shiphub.service.webhook.WebhookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WebhookServiceImp implements WebhookService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Override
    public void processEvent(Event event) {
        log.info("🔔 Processando evento: {} | ID: {}", event.getType(), event.getId());

        try {
            switch (event.getType()) {

                case "payment_intent.succeeded":
                    handlePaymentSucceeded(event);
                    break;

                case "payment_intent.payment_failed":
                    handlePaymentFailed(event);
                    break;

                case "payment_intent.canceled":
                    handlePaymentCanceled(event);
                    break;

                case "charge.refunded":
                    handleRefund(event);
                    break;

                case "payment_intent.created":
                    log.info("ℹ️ PaymentIntent criado: {}", event.getId());
                    break;

                case "payment_intent.processing":
                    log.info("⏳ Pagamento processando: {}", event.getId());
                    break;

                default:
                    log.info("ℹ️ Evento não tratado: {}", event.getType());
            }

        } catch (Exception e) {
            log.error("❌ Erro ao processar evento {}: {}", event.getType(), e.getMessage(), e);
            throw new RuntimeException("Erro ao processar webhook", e);
        }
    }

    @Override
    public void handlePaymentSucceeded(Event event) {
        PaymentIntent paymentIntent = deserializePaymentIntent(event);

        if (paymentIntent == null) {
            log.error("❌ Não foi possível deserializar PaymentIntent");
            return;
        }

        String paymentIntentId = paymentIntent.getId();
        Long amountReceived = paymentIntent.getAmountReceived();

        log.info("✅ Pagamento aprovado!");
        log.info("   PaymentIntent: {}", paymentIntentId);
        log.info("   Valor: {} centavos", amountReceived);

        Payment payment = paymentRepository
                .findByStripePaymentId(paymentIntentId)
                .orElse(null);

        if (payment == null) {
            log.error("⚠️ Pagamento não encontrado no banco: {}", paymentIntentId);
            return;
        }

        payment.setStatus(PaymentStatus.SUCCEEDED);
        paymentRepository.save(payment);
        log.info("💾 Payment status atualizado: SUCCEEDED");

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.PAYMENT_APPROVED);
        orderRepository.save(order);
        log.info("📦 Order status atualizado: PAYMENT_APPROVED");

        // - Enviar email de confirmação
        // - Gerar nota fiscal
        // - Notificar vendedor
        // - Atualizar estoque
        // - Criar label de envio

        log.info("🎉 Pagamento processado com sucesso!");
    }

    @Override
    public void handlePaymentFailed(Event event) {
        PaymentIntent paymentIntent = deserializePaymentIntent(event);

        if (paymentIntent == null) return;

        String paymentIntentId = paymentIntent.getId();
        String errorMessage = paymentIntent.getLastPaymentError() != null
                ? paymentIntent.getLastPaymentError().getMessage()
                : "Erro desconhecido";

        log.error("❌ Pagamento falhou!");
        log.error("   PaymentIntent: {}", paymentIntentId);
        log.error("   Motivo: {}", errorMessage);

        Payment payment = paymentRepository
                .findByStripePaymentId(paymentIntentId)
                .orElse(null);

        if (payment == null) {
            log.error("⚠️ Pagamento não encontrado: {}", paymentIntentId);
            return;
        }

        payment.setStatus(PaymentStatus.FAILED);
        payment.setErrorMessage(errorMessage);
        paymentRepository.save(payment);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.PAYMENT_FAILED);
        orderRepository.save(order);

        log.info("💾 Pagamento marcado como FAILED");
    }

    @Override
    public void handlePaymentCanceled(Event event) {
        PaymentIntent paymentIntent = deserializePaymentIntent(event);

        if (paymentIntent == null) return;

        String paymentIntentId = paymentIntent.getId();
        log.info("🚫 Pagamento cancelado: {}", paymentIntentId);

        Payment payment = paymentRepository
                .findByStripePaymentId(paymentIntentId)
                .orElse(null);

        if (payment == null) return;

        payment.setStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("💾 Pagamento e pedido cancelados");
    }

    @Override
    public void handleRefund(Event event) {
        log.info("↩️ Reembolso processado");

        // Implementar lógica de reembolso
        // - Atualizar status do pagamento para REFUNDED
        // - Atualizar status do pedido
        // - Devolver estoque
        // - Notificar cliente e vendedor
    }

    @Override
    public Event constructEventFromPayload(String payload, String signature) throws SignatureVerificationException {
        try {
            return Webhook.constructEvent(payload, signature, webhookSecret);
        } catch (SignatureVerificationException e) {
            log.error("⚠️ Assinatura de webhook inválida: {}", e.getMessage());
            throw e;
        }
    }

    private PaymentIntent deserializePaymentIntent(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer =
                event.getDataObjectDeserializer();

        if (dataObjectDeserializer.getObject().isEmpty()) {
            log.error("❌ Evento sem dados");
            return null;
        }

        StripeObject stripeObject = dataObjectDeserializer.getObject().get();

        if (stripeObject instanceof PaymentIntent) {
            return (PaymentIntent) stripeObject;
        }

        log.error("❌ Objeto não é PaymentIntent: {}", stripeObject.getClass());
        return null;
    }
}
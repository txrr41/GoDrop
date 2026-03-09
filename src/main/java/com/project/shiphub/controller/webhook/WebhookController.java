package com.project.shiphub.controller.webhook;

import com.project.shiphub.model.auth.DropperProfile;
import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderItem;
import com.project.shiphub.model.order.OrderStatus;
import com.project.shiphub.model.payment.DropperCommission;
import com.project.shiphub.repository.auth.DropperProfileRepository;
import com.project.shiphub.repository.order.OrderRepository;
import com.project.shiphub.repository.payment.DropperCommissionRepository;
import com.project.shiphub.service.auth.DropperService;
import com.project.shiphub.service.email.EmailService;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final OrderRepository orderRepository;
    private final DropperService dropperService;
    private final EmailService emailService;
    private final DropperCommissionRepository commissionRepository;
    private final DropperProfileRepository dropperProfileRepository;

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
        } catch (SignatureVerificationException e) {
            log.error("❌ Assinatura inválida no webhook");
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
            log.error("❌ Erro ao processar webhook", e);
            return ResponseEntity.status(500).body("error");
        }
    }

    private void handlePaymentSucceeded(Event event) throws EventDataObjectDeserializationException {
        PaymentIntent pi = (PaymentIntent) event.getDataObjectDeserializer().deserializeUnsafe();

        String orderIdStr = pi.getMetadata().get("order_id");
        if (orderIdStr == null) { log.error("❌ order_id ausente no metadata"); return; }

        Long orderId = Long.parseLong(orderIdStr);

        aprovarPedido(orderId);

        String dropperAccountId = pi.getMetadata().get("dropper_account");
        if (dropperAccountId != null) {
            registrarComissao(orderId, pi, dropperAccountId);
            atualizarNivelDropper(orderId);
        }

        try {
            Order order = orderRepository.findByIdWithItems(orderId)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
            emailService.sendOrderConfirmationEmail(order, LocalDateTime.now());
        } catch (Exception e) {
            log.warn("⚠️ Erro ao enviar email (não crítico): {}", e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void aprovarPedido(Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + orderId));
        order.setStatus(OrderStatus.PAYMENT_APPROVED);
        orderRepository.save(order);
        log.info("✅ Pedido #{} aprovado", orderId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarComissao(Long orderId, PaymentIntent pi, String stripeAccountId) {
        if (commissionRepository.findByOrderId(orderId).isPresent()) {
            log.warn("⚠️ Comissão do pedido #{} já registrada", orderId);
            return;
        }

        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + orderId));

        DropperProfile dropper = dropperProfileRepository
                .findByStripeAccountId(stripeAccountId)
                .orElse(null);

        if (dropper == null) {
            log.error("❌ Dropper não encontrado para conta Stripe: {}", stripeAccountId);
            return;
        }

        // ─── CÁLCULO DE MARGENS ───────────────────────────────────────────
        //
        // Exemplo com desconto de nível 15%:
        //
        //   Produto:         custo R$4000  |  dropper cobra R$5000
        //   Desconto 15%:    R$750 (sobre o total de R$5000)
        //   Cliente paga:    R$4250
        //
        //   Margem dropper   = customPrice - unitCost = R$5000 - R$4000 = R$1000
        //                      (dropper sempre recebe sua margem cheia)
        //
        //   GoDrop recebe    = totalPago - margemDropper = R$4250 - R$1000 = R$3250
        //                      (GoDrop absorve o desconto: R$4000 custo - R$750 desconto)
        //
        //   Desconto absorvido pela GoDrop = custoTotal - goDropRecebe
        //                                 = R$4000 - R$3250 = R$750 ✓

        BigDecimal totalPago     = order.getTotalAmount(); // quanto o cliente realmente pagou
        BigDecimal dropperMargin = BigDecimal.ZERO;
        BigDecimal custoTotal    = BigDecimal.ZERO;

        for (OrderItem item : order.getItems()) {
            BigDecimal custo      = item.getUnitPrice();   // preço de custo GoDrop
            BigDecimal customPrice = item.getCustomPrice() != null
                    ? item.getCustomPrice()
                    : item.getUnitPrice();
            int qty = item.getQuantity();

            BigDecimal margemItem = customPrice.subtract(custo)
                    .multiply(new BigDecimal(qty));
            BigDecimal custoItem  = custo.multiply(new BigDecimal(qty));

            dropperMargin = dropperMargin.add(margemItem);
            custoTotal    = custoTotal.add(custoItem);

            log.info("   {} x{} | custo R${} | customPrice R${} | margem dropper R${}",
                    item.getProduct().getNome(), qty, custo, customPrice,
                    margemItem);
        }

        // GoDrop recebe tudo que sobrar depois de pagar o dropper
        // Se houve desconto de nível, esse desconto sai do bolso da GoDrop
        BigDecimal goDropRecebe = totalPago.subtract(dropperMargin);

        // Desconto absorvido = custo normal - o que GoDrop realmente recebe
        BigDecimal descontoAbsorvido = custoTotal.subtract(goDropRecebe);
        if (descontoAbsorvido.compareTo(BigDecimal.ZERO) < 0) {
            descontoAbsorvido = BigDecimal.ZERO;
        }

        log.info("💰 Pedido #{} | clientePagou=R${} | margemDropper=R${} | goDropRecebe=R${} | descontoAbsorvido=R${}",
                orderId, totalPago, dropperMargin, goDropRecebe, descontoAbsorvido);

        DropperCommission commission = DropperCommission.builder()
                .dropperProfile(dropper)
                .order(order)
                .totalAmount(totalPago)       // total pago pelo cliente
                .costAmount(goDropRecebe)     // o que a GoDrop efetivamente recebeu
                .platformFee(descontoAbsorvido) // desconto que a GoDrop absorveu (para auditoria)
                .dropperMargin(dropperMargin)  // lucro real do dropper
                .status(DropperCommission.CommissionStatus.PAID)
                .stripeTransferId(pi.getId())
                .build();

        commissionRepository.save(commission);
        commissionRepository.flush();
        log.info("✅ Comissão salva — dropper ganha R${} | GoDrop recebe R${}",
                dropperMargin, goDropRecebe);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void atualizarNivelDropper(Long orderId) {
        commissionRepository.findByOrderId(orderId).ifPresent(commission -> {
            try {
                dropperService.updateSalesAndLevel(
                        commission.getDropperProfile().getUser().getId(),
                        commission.getDropperMargin()
                );
                log.info("✅ Nível atualizado — margem acumulada R${}", commission.getDropperMargin());
            } catch (Exception e) {
                log.warn("⚠️ Erro ao atualizar nível: {}", e.getMessage());
            }
        });
    }

    private void handlePaymentFailed(Event event) {
        PaymentIntent pi = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject().orElseThrow(() -> new RuntimeException("Falha ao deserializar"));

        String orderIdStr = pi.getMetadata().get("order_id");
        if (orderIdStr == null) return;

        Order order = orderRepository.findById(Long.parseLong(orderIdStr)).orElse(null);
        if (order == null) return;

        order.setStatus(OrderStatus.PAYMENT_FAILED);
        orderRepository.save(order);
        log.info("❌ Pedido #{} marcado como PAYMENT_FAILED", order.getId());
    }
}
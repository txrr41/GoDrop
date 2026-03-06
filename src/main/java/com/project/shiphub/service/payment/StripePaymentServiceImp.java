package com.project.shiphub.service.payment;

import com.project.shiphub.dto.payment.CreatePaymentRequest;
import com.project.shiphub.dto.payment.PaymentResponse;
import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderStatus;
import com.project.shiphub.model.payment.Payment;
import com.project.shiphub.model.payment.PaymentMethod;
import com.project.shiphub.model.payment.PaymentStatus;
import com.project.shiphub.repository.order.OrderRepository;
import com.project.shiphub.repository.payment.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StripePaymentServiceImp implements StripePaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        log.info("💳 Criando pagamento para pedido: {}", request.getOrderId());

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + request.getOrderId()));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Pedido já foi processado: " + order.getId());
        }

        long totalCents         = request.getAmountInCents();
        String dropperAccountId = request.getDropperStripeAccountId();
        Long dropperMargin      = request.getDropperMarginInCents();

        log.info("💰 Total: {} centavos | Conta dropper: {} | Margem: {} centavos",
                totalCents,
                dropperAccountId != null ? dropperAccountId : "sem split",
                dropperMargin != null ? dropperMargin : 0);

        try {
            PaymentIntent paymentIntent = buildPaymentIntent(
                    totalCents,
                    request.getPaymentMethod(),
                    request.getCustomerEmail(),
                    order.getId(),
                    dropperAccountId,
                    dropperMargin
            );

            log.info("✅ PaymentIntent criado: {}", paymentIntent.getId());

            Payment payment = new Payment();
            payment.setStripePaymentId(paymentIntent.getId());
            payment.setOrder(order);
            payment.setAmountInCents(totalCents);
            payment.setCurrency("BRL");
            payment.setStatus(PaymentStatus.PENDING);
            payment.setMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
            payment.setPlatformFeeInCents(dropperMargin != null ? totalCents - dropperMargin : totalCents);

            return new PaymentResponse(paymentRepository.save(payment), paymentIntent.getClientSecret());

        } catch (StripeException e) {
            log.error("❌ Erro Stripe: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao processar pagamento: " + e.getMessage());
        }
    }

    private PaymentIntent buildPaymentIntent(
            long amountCents,
            String paymentMethod,
            String customerEmail,
            Long orderId,
            String dropperAccountId,
            Long dropperMarginCents
    ) throws StripeException {

        Map<String, String> metadata = new HashMap<>();
        metadata.put("order_id", orderId.toString());
        if (dropperAccountId != null) {
            metadata.put("dropper_account", dropperAccountId);
            metadata.put("dropper_margin_cents", String.valueOf(dropperMarginCents));
        }

        PaymentIntentCreateParams.Builder params = PaymentIntentCreateParams.builder()
                .setAmount(amountCents)
                .setCurrency("brl")
                .setReceiptEmail(customerEmail)
                .putAllMetadata(metadata);

        switch (paymentMethod.toUpperCase()) {
            case "CREDIT_CARD", "DEBIT_CARD" -> params.addPaymentMethodType("card");
            case "PIX"    -> params.addPaymentMethodType("pix");
            case "BOLETO" -> params.addPaymentMethodType("boleto");
            default -> throw new IllegalArgumentException("Método inválido: " + paymentMethod);
        }

        if (dropperAccountId != null && dropperMarginCents != null && dropperMarginCents > 0) {
            params.setTransferData(
                    PaymentIntentCreateParams.TransferData.builder()
                            .setDestination(dropperAccountId)
                            .setAmount(dropperMarginCents) // só a margem vai pro dropper
                            .build()
            );

            log.info("💸 Split: total={} | dropper recebe={} | plataforma retém={}",
                    amountCents, dropperMarginCents, amountCents - dropperMarginCents);
        }

        return PaymentIntent.create(params.build());
    }

    @Override
    public void confirmPayment(String paymentIntentId) {
        Payment payment = paymentRepository.findByStripePaymentId(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
        payment.setStatus(PaymentStatus.SUCCEEDED);
        paymentRepository.save(payment);

        Order order = orderRepository.findByIdWithItems(payment.getOrder().getId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        order.setStatus(OrderStatus.PAYMENT_APPROVED);
        orderRepository.save(order);
    }

    @Override
    public void failPayment(String paymentIntentId, String errorMessage) {
        Payment payment = paymentRepository.findByStripePaymentId(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
        payment.setStatus(PaymentStatus.FAILED);
        payment.setErrorMessage(errorMessage);
        paymentRepository.save(payment);
        payment.getOrder().setStatus(OrderStatus.CANCELLED);
        orderRepository.save(payment.getOrder());
    }

    @Override
    public PaymentResponse refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
        if (payment.getStatus() != PaymentStatus.SUCCEEDED) {
            throw new RuntimeException("Apenas pagamentos confirmados podem ser reembolsados");
        }
        try {
            PaymentIntent.retrieve(payment.getStripePaymentId());
            payment.setStatus(PaymentStatus.REFUNDED);
            return new PaymentResponse(paymentRepository.save(payment));
        } catch (StripeException e) {
            throw new RuntimeException("Erro ao reembolsar: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponse getPayment(Long paymentId) {
        return new PaymentResponse(paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado")));
    }

    @Override
    public PaymentResponse getPaymentByStripeId(String stripePaymentId) {
        return new PaymentResponse(paymentRepository.findByStripePaymentId(stripePaymentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado")));
    }
}
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StripePaymentServiceImp implements StripePaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Value("${stripe.platform.fee.percent:30}")
    private int platformFeePercent;

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        log.info("💳 Criando pagamento para pedido: {}", request.getOrderId());

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> {
                    log.error("❌ Pedido não encontrado: {}", request.getOrderId());
                    return new RuntimeException("Pedido não encontrado");
                });

        if (order.getStatus() != OrderStatus.PENDING) {
            log.error("❌ Pedido já foi processado: {}", order.getId());
            throw new RuntimeException("Pedido já foi processado");
        }

        long totalCents = request.getAmountInCents();
        long platformFee = calculatePlatformFee(totalCents);

        log.info("💰 Total: {} centavos | Taxa: {} centavos", totalCents, platformFee);

        try {
            PaymentIntent paymentIntent = createStripePaymentIntent(
                    totalCents,
                    request.getPaymentMethod(),
                    request.getCustomerEmail(),
                    order.getId(),
                    platformFee,
                    null
            );

            log.info("✅ PaymentIntent criado: {}", paymentIntent.getId());
            Payment payment = new Payment();
            payment.setStripePaymentId(paymentIntent.getId());
            payment.setOrder(order);
            payment.setAmountInCents(totalCents);
            payment.setCurrency("BRL");
            payment.setStatus(PaymentStatus.PENDING);
            payment.setMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
            payment.setPlatformFeeInCents(platformFee);

            Payment savedPayment = paymentRepository.save(payment);
            log.info("💾 Pagamento salvo no banco: {}", savedPayment.getId());

            return new PaymentResponse(savedPayment, paymentIntent.getClientSecret());

        } catch (StripeException e) {
            log.error("❌ Erro ao criar pagamento no Stripe: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao processar pagamento: " + e.getMessage());
        }
    }

    @Override
    public void confirmPayment(String paymentIntentId) {
        log.info("✅ Confirmando pagamento: {}", paymentIntentId);

        Payment payment = paymentRepository.findByStripePaymentId(paymentIntentId)
                .orElseThrow(() -> {
                    log.error("❌ Pagamento não encontrado: {}", paymentIntentId);
                    return new RuntimeException("Pagamento não encontrado");
                });

        payment.setStatus(PaymentStatus.SUCCEEDED);
        paymentRepository.save(payment);

        Order order = orderRepository.findByIdWithItems(payment.getOrder().getId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        order.setStatus(OrderStatus.PAYMENT_APPROVED);
        orderRepository.save(order);

        log.info("✅ Pagamento confirmado com sucesso!");
    }

    @Override
    public void failPayment(String paymentIntentId, String errorMessage) {
        log.error("❌ Pagamento falhou: {} - {}", paymentIntentId, errorMessage);

        Payment payment = paymentRepository.findByStripePaymentId(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        payment.setStatus(PaymentStatus.FAILED);
        payment.setErrorMessage(errorMessage);
        paymentRepository.save(payment);

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public PaymentResponse refundPayment(Long paymentId) {
        log.info("↩️ Processando reembolso para pagamento: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        if (payment.getStatus() != PaymentStatus.SUCCEEDED) {
            throw new RuntimeException("Apenas pagamentos confirmados podem ser reembolsados");
        }

        try {
            PaymentIntent.retrieve(payment.getStripePaymentId());

            payment.setStatus(PaymentStatus.REFUNDED);
            Payment savedPayment = paymentRepository.save(payment);

            log.info("✅ Reembolso processado com sucesso");
            return new PaymentResponse(savedPayment);

        } catch (StripeException e) {
            log.error("❌ Erro ao processar reembolso: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao processar reembolso: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponse getPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
        return new PaymentResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByStripeId(String stripePaymentId) {
        Payment payment = paymentRepository.findByStripePaymentId(stripePaymentId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
        return new PaymentResponse(payment);
    }

    private PaymentIntent createStripePaymentIntent(
            Long amountCents,
            String paymentMethod,
            String customerEmail,
            Long orderId,
            Long platformFee,
            String sellerAccountId
    ) throws StripeException {

        Map<String, String> metadata = new HashMap<>();
        metadata.put("order_id", orderId.toString());  // ✅ snake_case
        metadata.put("platform_fee", platformFee.toString());

        log.info("📋 Metadata do PaymentIntent: {}", metadata);

        PaymentIntentCreateParams.Builder paramsBuilder =
                PaymentIntentCreateParams.builder()
                        .setAmount(amountCents)
                        .setCurrency("brl")
                        .setReceiptEmail(customerEmail)
                        .putAllMetadata(metadata);

        switch (paymentMethod.toUpperCase()) {
            case "CREDIT_CARD":
            case "DEBIT_CARD":
                paramsBuilder.addPaymentMethodType("card");
                break;
            case "PIX":
                paramsBuilder.addPaymentMethodType("pix");
                break;
            case "BOLETO":
                paramsBuilder.addPaymentMethodType("boleto");
                break;
            default:
                throw new IllegalArgumentException("Método de pagamento inválido: " + paymentMethod);
        }

        if (sellerAccountId != null) {
            paramsBuilder
                    .setApplicationFeeAmount(platformFee)
                    .setTransferData(
                            PaymentIntentCreateParams.TransferData.builder()
                                    .setDestination(sellerAccountId)
                                    .build()
                    );
        }

        return PaymentIntent.create(paramsBuilder.build());
    }

    private long calculatePlatformFee(long totalAmountCents) {
        BigDecimal amount = new BigDecimal(totalAmountCents);
        BigDecimal feePercent = new BigDecimal(platformFeePercent);

        BigDecimal fee = amount
                .multiply(feePercent)
                .divide(new BigDecimal(100), 0, RoundingMode.HALF_UP);

        return fee.longValue();
    }
}
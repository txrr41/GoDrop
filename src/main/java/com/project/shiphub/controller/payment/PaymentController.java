package com.project.shiphub.controller.payment;

import com.project.shiphub.dto.payment.CreatePaymentRequest;
import com.project.shiphub.dto.payment.PaymentResponse;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.product.Product;
import com.project.shiphub.model.store.DropperStore;
import com.project.shiphub.model.store.StoreProduct;
import com.project.shiphub.repository.store.DropperStoreRepository;
import com.project.shiphub.service.auth.DropperService;
import com.project.shiphub.service.order.OrderService;
import com.project.shiphub.service.payment.StripePaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final StripePaymentService stripePaymentService;
    private final OrderService orderService;
    private final DropperService dropperService;
    private final DropperStoreRepository dropperStoreRepository;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request,
            Authentication authentication
    ) {
        if (authentication == null) return ResponseEntity.status(401).build();

        try {
            User user = (User) authentication.getPrincipal();

            // Aplica desconto de nível do dropper
            int discount = dropperService.getDiscount(user.getId());
            if (discount > 0) {
                long original = request.getAmountInCents();
                BigDecimal discounted = new BigDecimal(original)
                        .multiply(new BigDecimal(100 - discount))
                        .divide(new BigDecimal(100), 0, RoundingMode.HALF_UP);
                request.setAmountInCents(discounted.longValue());
                log.info("Desconto dropper {}% aplicado: {} → {}", discount, original, discounted);
            }

            // Se for compra em loja de dropper, calcula margem e configura split
            if (request.getStoreSlug() != null && !request.getStoreSlug().isBlank()) {
                dropperStoreRepository.findActiveBySlug(request.getStoreSlug())
                        .ifPresent(store -> configurarSplit(store, request));
            }

            BigDecimal total = BigDecimal.valueOf(request.getAmountInCents())
                    .divide(new BigDecimal(100));

            Order order = orderService.createOrder(user, total, request);
            request.setOrderId(order.getId());
            request.setBuyerEmail(order.getBuyerEmail());

            PaymentResponse response = stripePaymentService.createPayment(request);
            log.info("✅ Pagamento criado para pedido #{}", order.getId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ Erro ao processar pagamento", e);
            return ResponseEntity.badRequest().build();
        }
    }

    private void configurarSplit(DropperStore store, CreatePaymentRequest request) {
        String stripeAccountId = store.getDropperProfile().getStripeAccountId();
        Boolean chargesEnabled = store.getDropperProfile().getStripeChargesEnabled();

        if (stripeAccountId == null || !Boolean.TRUE.equals(chargesEnabled)) {
            log.warn("⚠️ Dropper da loja '{}' sem Stripe ativo — sem split", request.getStoreSlug());
            return;
        }

        Map<Long, BigDecimal> customPrices = store.getStoreProducts().stream()
                .collect(Collectors.toMap(
                        sp -> sp.getProduct().getId(),
                        StoreProduct::getCustomPrice
                ));

        BigDecimal margemTotal = BigDecimal.ZERO;

        for (CreatePaymentRequest.CartItemDTO item : request.getItems()) {
            BigDecimal customPrice = customPrices.get(item.getProductId());
            if (customPrice == null) continue;

            Product produto = store.getStoreProducts().stream()
                    .filter(sp -> sp.getProduct().getId().equals(item.getProductId()))
                    .map(StoreProduct::getProduct)
                    .findFirst()
                    .orElse(null);

            if (produto == null) continue;

            BigDecimal custo = produto.getPreco();
            BigDecimal margem = customPrice.subtract(custo)
                    .multiply(new BigDecimal(item.getQuantity()));

            if (margem.compareTo(BigDecimal.ZERO) > 0) {
                margemTotal = margemTotal.add(margem);
            }
        }

        long margemCentavos = margemTotal
                .multiply(new BigDecimal(100))
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();

        if (margemCentavos <= 0) {
            log.warn("⚠️ Margem do dropper é zero ou negativa — sem split");
            return;
        }

        request.setDropperStripeAccountId(stripeAccountId);
        request.setDropperMarginInCents(margemCentavos);

        log.info("💸 Split configurado: conta={}, margem={} centavos",
                stripeAccountId, margemCentavos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(stripePaymentService.refundPayment(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
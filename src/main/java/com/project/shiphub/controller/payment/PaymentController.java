package com.project.shiphub.controller.payment;

import com.project.shiphub.dto.payment.CreatePaymentRequest;
import com.project.shiphub.dto.payment.PaymentResponse;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.order.Order;
import com.project.shiphub.service.order.OrderService;
import com.project.shiphub.service.payment.StripePaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final StripePaymentService stripePaymentService;

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request,
            Authentication authentication
    ) {
        if (authentication == null) return ResponseEntity.status(401).build();

        try {
            User user = (User) authentication.getPrincipal();

            BigDecimal total = BigDecimal.valueOf(request.getAmountInCents()).divide(new BigDecimal(100));
            Order order = orderService.createOrder(user, total, request);
            request.setOrderId(order.getId());
            PaymentResponse response = stripePaymentService.createPayment(request);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erro ao processar pedido/pagamento", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable Long id) {
        try {
            PaymentResponse response = stripePaymentService.refundPayment(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
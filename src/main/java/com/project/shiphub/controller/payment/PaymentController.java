package com.project.shiphub.controller.payment;

import com.project.shiphub.service.payment.PixPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PixPaymentService pixPaymentService;

    @PostMapping("/test-pix")
    public String testPix() {
        pixPaymentService.createPixPayment();
        return "Teste PIX executado! Verifique os logs.";
    }
}
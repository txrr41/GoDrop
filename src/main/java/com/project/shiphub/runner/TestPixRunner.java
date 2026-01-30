package com.project.shiphub.runner;

import com.project.shiphub.service.payment.PixPaymentService;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestPixRunner implements CommandLineRunner {

    private final PixPaymentService pixPaymentService;

    public TestPixRunner(PixPaymentService pixPaymentService) {
        this.pixPaymentService = pixPaymentService;
    }

    @Override
    public void run(String @NonNull ... args) {
        System.out.println("=== TESTE PIX VIA RUNNER ===");
        pixPaymentService.createPixPayment();
    }
}


package com.project.shiphub.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "test.pix.enabled", havingValue = "true")
public class TestPixRunner implements CommandLineRunner {

    private final PixPaymentService pixPaymentService;

    public TestPixRunner(PixPaymentService pixPaymentService) {
        this.pixPaymentService = pixPaymentService;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== TESTE PIX VIA RUNNER ===");
        pixPaymentService.createPixPayment();
    }
}
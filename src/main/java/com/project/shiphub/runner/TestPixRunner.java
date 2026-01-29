package com.project.shiphub.runner;

import com.project.shiphub.service.payment.PixPayment;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestPixRunner implements CommandLineRunner {
    private final PixPayment pixPayment;

    public TestPixRunner(PixPayment pixPayment) {
        this.pixPayment = pixPayment;
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        pixPayment.createPixPayment();
    }
}

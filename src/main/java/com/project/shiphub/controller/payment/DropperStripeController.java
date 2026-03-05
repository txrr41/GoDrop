package com.project.shiphub.controller.payment;

import com.project.shiphub.service.payment.DropperStripeServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dropper/stripe")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DROPPER')")
public class DropperStripeController {

    private final DropperStripeServiceImp dropperStripeService;

    @PostMapping("/onboarding")
    public ResponseEntity<Map<String, String>> startOnboarding() throws Exception {
        return ResponseEntity.ok(dropperStripeService.createOnboardingLink());
    }

    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncAccount() throws Exception {
        return ResponseEntity.ok(dropperStripeService.syncStripeAccount());
    }
}
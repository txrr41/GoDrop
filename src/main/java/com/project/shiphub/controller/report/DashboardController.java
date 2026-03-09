package com.project.shiphub.controller.report;

import com.project.shiphub.service.report.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).build();
        log.info("📊 Dashboard acessado");
        return ResponseEntity.ok(dashboardService.getStats());
    }
}

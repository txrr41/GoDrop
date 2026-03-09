package com.project.shiphub.controller.report;

import com.project.shiphub.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/relatorio/comissoes")
    public ResponseEntity<byte[]> createCommissionReport(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        try {
            byte[] pdf = reportService.generateCommissionReport(startDate, endDate);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "relatorio_comissoes.pdf");
            return ResponseEntity.ok().headers(headers).body(pdf);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/relatorio/top-produtos")
    public ResponseEntity<byte[]> topProductsReport(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        try {
            byte[] pdf = reportService.generateTopProductsReport(startDate, endDate);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "top-produtos.pdf");
            return ResponseEntity.ok().headers(headers).body(pdf);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/relatorio/estoque-critico")
    public ResponseEntity<byte[]> criticalStockReport() {
        try {
            byte[] pdf = reportService.generateCriticalStockReport();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "estoque-critico.pdf");
            return ResponseEntity.ok().headers(headers).body(pdf);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
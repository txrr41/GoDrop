package com.project.shiphub.controller;

import com.project.shiphub.dto.offer.OfferDTO;
import com.project.shiphub.dto.offer.OfferRequest;
import com.project.shiphub.service.offer.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
@Slf4j
public class OfferController {

    private final OfferService offerService;

    @PostMapping
    public ResponseEntity<OfferDTO> createOffer(@Valid @RequestBody OfferRequest request) {
        log.info("📥 POST /api/offers - Criando oferta");
        OfferDTO offer = offerService.createOffer(request);
        return ResponseEntity.ok(offer);
    }

    @GetMapping
    public ResponseEntity<List<OfferDTO>> getAllOffers() {
        log.info("📥 GET /api/offers - Listando ofertas");
        List<OfferDTO> offers = offerService.getAllOffers();
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable Long id) {
        log.info("📥 GET /api/offers/{} - Buscando oferta", id);
        OfferDTO offer = offerService.getOfferById(id);
        return ResponseEntity.ok(offer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateOffer(
            @PathVariable Long id,
            @Valid @RequestBody OfferRequest request
    ) {
        log.info("📥 PUT /api/offers/{} - Atualizando oferta", id);
        OfferDTO offer = offerService.updateOffer(id, request);
        return ResponseEntity.ok(offer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        log.info("📥 DELETE /api/offers/{} - Deletando oferta", id);
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<OfferDTO> toggleOfferStatus(@PathVariable Long id) {
        log.info("📥 PATCH /api/offers/{}/toggle - Alternando status", id);
        OfferDTO offer = offerService.toggleOfferStatus(id);
        return ResponseEntity.ok(offer);
    }

    @GetMapping("/product/{productId}/price")
    public ResponseEntity<BigDecimal> getProductFinalPrice(@PathVariable Long productId) {
        log.info("📥 GET /api/offers/product/{}/price - Calculando preço final", productId);
        BigDecimal finalPrice = offerService.getProductFinalPrice(productId);
        return ResponseEntity.ok(finalPrice);
    }

    @GetMapping("/product/{productId}/best-offer")
    public ResponseEntity<OfferDTO> getBestOfferForProduct(@PathVariable Long productId) {
        log.info("📥 GET /api/offers/product/{}/best-offer - Buscando melhor oferta", productId);
        OfferDTO offer = offerService.getBestOfferForProduct(productId);
        return ResponseEntity.ok(offer);
    }
}
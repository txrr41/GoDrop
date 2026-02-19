package com.project.shiphub.service.offer;

import com.project.shiphub.dto.offer.OfferDTO;
import com.project.shiphub.dto.offer.OfferRequest;
import com.project.shiphub.model.offer.Offer;
import com.project.shiphub.model.offer.OfferStatus;
import com.project.shiphub.model.product.Product;
import com.project.shiphub.repository.offer.OfferRepository;
import com.project.shiphub.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OfferService implements OfferServicee {

    private final OfferRepository offerRepository;
    private final ProductRepository productRepository;

    public OfferDTO createOffer(OfferRequest request) {
        log.info("🎁 Criando oferta: {}", request.getName());

        validateOfferDates(request.getStartDate(), request.getEndDate());

        Offer offer = new Offer();
        offer.setName(request.getName());
        offer.setDescription(request.getDescription());
        offer.setType(request.getType());
        offer.setDiscountValue(request.getDiscountValue());
        offer.setStartDate(request.getStartDate());
        offer.setEndDate(request.getEndDate());
        offer.setActive(request.getActive());
        offer.setCategory(request.getCategory());
        offer.setStockLimit(request.getStockLimit());
        offer.setPriority(request.getPriority());

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(request.getStartDate()) && now.isBefore(request.getEndDate())) {
            offer.setStatus(OfferStatus.ACTIVE);
        } else {
            offer.setStatus(OfferStatus.SCHEDULED);
        }

        if (request.getProductIds() != null && !request.getProductIds().isEmpty()) {
            List<Product> products = productRepository.findAllById(request.getProductIds());
            offer.setProducts(products);
            log.info("✅ {} produtos vinculados à oferta", products.size());
        }

        Offer savedOffer = offerRepository.save(offer);
        log.info("✅ Oferta criada: ID {}", savedOffer.getId());

        return new OfferDTO(savedOffer);
    }

    public OfferDTO updateOffer(Long id, OfferRequest request) {
        log.info("📝 Atualizando oferta ID: {}", id);

        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferta não encontrada"));

        validateOfferDates(request.getStartDate(), request.getEndDate());

        offer.setName(request.getName());
        offer.setDescription(request.getDescription());
        offer.setType(request.getType());
        offer.setDiscountValue(request.getDiscountValue());
        offer.setStartDate(request.getStartDate());
        offer.setEndDate(request.getEndDate());
        offer.setActive(request.getActive());
        offer.setCategory(request.getCategory());
        offer.setStockLimit(request.getStockLimit());
        offer.setPriority(request.getPriority());

        if (request.getProductIds() != null) {
            List<Product> products = productRepository.findAllById(request.getProductIds());
            offer.setProducts(products);
        }

        Offer updated = offerRepository.save(offer);
        log.info("✅ Oferta atualizada");

        return new OfferDTO(updated);
    }

    public List<OfferDTO> getAllOffers() {
        return offerRepository.findAll().stream()
                .map(OfferDTO::new)
                .collect(Collectors.toList());
    }

    public OfferDTO getOfferById(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferta não encontrada"));
        return new OfferDTO(offer);
    }

    public void deleteOffer(Long id) {
        log.info("🗑️ Deletando oferta ID: {}", id);
        offerRepository.deleteById(id);
    }

    public OfferDTO toggleOfferStatus(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferta não encontrada"));

        offer.setActive(!offer.getActive());
        Offer updated = offerRepository.save(offer);

        log.info("🔄 Oferta {} {}", id, updated.getActive() ? "ativada" : "desativada");

        return new OfferDTO(updated);
    }

    @Scheduled(fixedRate = 60000) // Executa a cada 1 minuto
    public void updateOfferStatuses() {
        LocalDateTime now = LocalDateTime.now();

        // Ativar ofertas agendadas
        List<Offer> toActivate = offerRepository.findOffersToActivate(now);
        for (Offer offer : toActivate) {
            offer.setStatus(OfferStatus.ACTIVE);
            offerRepository.save(offer);
            log.info("✅ Oferta {} ATIVADA automaticamente", offer.getId());
        }

        // Expirar ofertas ativas
        List<Offer> toExpire = offerRepository.findOffersToExpire(now);
        for (Offer offer : toExpire) {
            offer.setStatus(OfferStatus.EXPIRED);
            offerRepository.save(offer);
            log.info("⏰ Oferta {} EXPIRADA automaticamente", offer.getId());
        }

        // Expirar ofertas que atingiram limite de estoque
        List<Offer> stockLimitReached = offerRepository.findOffersWithStockLimitReached();
        for (Offer offer : stockLimitReached) {
            offer.setStatus(OfferStatus.EXPIRED);
            offerRepository.save(offer);
            log.info("📦 Oferta {} EXPIRADA por limite de estoque", offer.getId());
        }
    }

    public BigDecimal getProductFinalPrice(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        BigDecimal originalPrice = product.getPreco();

        List<Offer> activeOffers = offerRepository.findActiveOffersByProductId(productId);

        if (product.getCategoria() != null) {
            List<Offer> categoryOffers = offerRepository.findActiveOffersByCategory(product.getCategoria());
            activeOffers.addAll(categoryOffers);
        }

        if (activeOffers.isEmpty()) {
            return originalPrice;
        }

        activeOffers = activeOffers.stream()
                .filter(Offer::isActive)
                .collect(Collectors.toList());

        if (activeOffers.isEmpty()) {
            return originalPrice;
        }

        Offer bestOffer = activeOffers.stream()
                .max((o1, o2) -> {
                    BigDecimal discount1 = o1.calculateDiscount(originalPrice);
                    BigDecimal discount2 = o2.calculateDiscount(originalPrice);
                    return discount1.compareTo(discount2);
                })
                .orElse(null);

        if (bestOffer != null) {
            return bestOffer.calculateFinalPrice(originalPrice);
        }

        return originalPrice;
    }

    public OfferDTO getBestOfferForProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        BigDecimal originalPrice = product.getPreco();

        List<Offer> activeOffers = offerRepository.findActiveOffersByProductId(productId);

        if (product.getCategoria() != null) {
            List<Offer> categoryOffers = offerRepository.findActiveOffersByCategory(product.getCategoria());
            activeOffers.addAll(categoryOffers);
        }

        if (activeOffers.isEmpty()) {
            return null;
        }

        activeOffers = activeOffers.stream()
                .filter(Offer::isActive)
                .collect(Collectors.toList());

        Offer bestOffer = activeOffers.stream()
                .max((o1, o2) -> {
                    BigDecimal discount1 = o1.calculateDiscount(originalPrice);
                    BigDecimal discount2 = o2.calculateDiscount(originalPrice);
                    return discount1.compareTo(discount2);
                })
                .orElse(null);

        return bestOffer != null ? new OfferDTO(bestOffer) : null;
    }

    private void validateOfferDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("Data de início deve ser anterior à data de fim");
        }
    }
}
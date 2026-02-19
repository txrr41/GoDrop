package com.project.shiphub.service.offer;

import com.project.shiphub.dto.offer.OfferDTO;
import com.project.shiphub.dto.offer.OfferRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OfferServicee {
    OfferDTO createOffer (OfferRequest request);
    OfferDTO updateOffer (Long id, OfferRequest request);
    List<OfferDTO> getAllOffers ();
    OfferDTO getOfferById (Long id);
    void deleteOffer (Long id);
    OfferDTO toggleOfferStatus (Long id);
    void updateOfferStatuses ();
    BigDecimal getProductFinalPrice (Long productId);
    OfferDTO getBestOfferForProduct (Long productId);

}

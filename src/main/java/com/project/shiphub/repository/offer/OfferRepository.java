package com.project.shiphub.repository.offer;

import com.project.shiphub.model.offer.Offer;
import com.project.shiphub.model.offer.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByStatus(OfferStatus status);

    List<Offer> findByActiveTrue();

    @Query("SELECT o FROM Offer o WHERE o.active = true AND o.status = 'SCHEDULED' " +
            "AND o.startDate <= :now AND o.endDate > :now")
    List<Offer> findOffersToActivate(@Param("now") LocalDateTime now);

    @Query("SELECT o FROM Offer o WHERE o.status = 'ACTIVE' AND o.endDate <= :now")
    List<Offer> findOffersToExpire(@Param("now") LocalDateTime now);

    @Query("SELECT o FROM Offer o WHERE o.status = 'ACTIVE' " +
            "AND o.stockLimit IS NOT NULL AND o.usedStock >= o.stockLimit")
    List<Offer> findOffersWithStockLimitReached();

    @Query("SELECT o FROM Offer o JOIN o.products p WHERE p.id = :productId " +
            "AND o.active = true AND o.status = 'ACTIVE' " +
            "ORDER BY o.priority DESC")
    List<Offer> findActiveOffersByProductId(@Param("productId") Long productId);

    @Query("SELECT o FROM Offer o WHERE o.category = :category " +
            "AND o.active = true AND o.status = 'ACTIVE' " +
            "ORDER BY o.priority DESC")
    List<Offer> findActiveOffersByCategory(@Param("category") String category);
}
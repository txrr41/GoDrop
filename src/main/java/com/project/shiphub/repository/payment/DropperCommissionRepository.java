package com.project.shiphub.repository.payment;

import com.project.shiphub.model.payment.DropperCommission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DropperCommissionRepository extends JpaRepository<DropperCommission, Long> {

    List<DropperCommission> findByDropperProfileIdOrderByCreatedAtDesc(Long dropperProfileId);

    Optional<DropperCommission> findByOrderId(Long orderId);

    @Query("SELECT COALESCE(SUM(c.dropperMargin), 0) FROM DropperCommission c " +
            "WHERE c.dropperProfile.id = :dropperProfileId " +
            "AND c.status = 'PAID'")
    BigDecimal sumPaidMarginByDropperId(@Param("dropperProfileId") Long dropperProfileId);

    @Query("SELECT COALESCE(SUM(c.platformFee), 0) FROM DropperCommission c " +
            "WHERE c.status = 'PAID'")
    BigDecimal sumAllPlatformFees();

    List<DropperCommission> findByStatus(DropperCommission.CommissionStatus status);
}
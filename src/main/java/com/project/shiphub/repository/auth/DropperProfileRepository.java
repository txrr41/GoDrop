package com.project.shiphub.repository.auth;

import com.project.shiphub.model.auth.DropperProfile;
import com.project.shiphub.model.auth.DropperStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DropperProfileRepository extends JpaRepository<DropperProfile, Long> {
    Optional<DropperProfile> findByUserId(Long userId);

    boolean existsByCnpj(String cnpj);

    List<DropperProfile> findByStatus(DropperStatus status);

    Optional<DropperProfile> findByStripeAccountId(String stripeAccountId);
}

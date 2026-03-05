package com.project.shiphub.model.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "dropper_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DropperProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false, length = 20)
    private String whatsapp;

    @Column(nullable = false)
    @Builder.Default
    private Long xp = 0L;

    @Enumerated(EnumType.STRING)
    private DropperLevel level;

    @Column(name = "total_sales", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalSales = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DropperStatus status = DropperStatus.PENDING;

    @Column(name = "stripe_account_id", unique = true, length = 100)
    private String stripeAccountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "stripe_account_status")
    @Builder.Default
    private DropperStripeStatus stripeAccountStatus = DropperStripeStatus.NOT_CONNECTED;

    @Column(name = "stripe_charges_enabled", nullable = false)
    @Builder.Default
    private Boolean stripeChargesEnabled = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
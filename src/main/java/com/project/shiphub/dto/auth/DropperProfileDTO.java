package com.project.shiphub.dto.auth;

import com.project.shiphub.model.auth.DropperLevel;
import com.project.shiphub.model.auth.DropperProfile;
import com.project.shiphub.model.auth.DropperStatus;
import com.project.shiphub.model.auth.DropperStripeStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DropperProfileDTO {

    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String cnpj;
    private String storeName;
    private String whatsapp;
    private Long xp;
    private DropperLevel level;
    private DropperStatus status;
    private BigDecimal totalSales;
    private int discountPercent;
    private LocalDateTime createdAt;

    // Stripe Connect
    private String stripeAccountId;
    private DropperStripeStatus stripeAccountStatus;
    private boolean stripeChargesEnabled;

    public DropperProfileDTO(DropperProfile profile) {
        this.id             = profile.getId();
        this.userId         = profile.getUser().getId();
        this.userName       = profile.getUser().getName();
        this.userEmail      = profile.getUser().getEmail();
        this.cnpj           = profile.getCnpj();
        this.storeName      = profile.getStoreName();
        this.whatsapp       = profile.getWhatsapp();
        this.xp             = profile.getXp();
        this.level          = profile.getLevel();
        this.status         = profile.getStatus();
        this.totalSales     = profile.getTotalSales();
        this.discountPercent = calculateDiscount(profile.getLevel());
        this.createdAt      = profile.getCreatedAt();
        this.stripeAccountId      = profile.getStripeAccountId();
        this.stripeAccountStatus  = profile.getStripeAccountStatus() != null
                ? profile.getStripeAccountStatus()
                : DropperStripeStatus.NOT_CONNECTED;
        this.stripeChargesEnabled = Boolean.TRUE.equals(profile.getStripeChargesEnabled());
    }

    private int calculateDiscount(DropperLevel level) {
        if (level == null) return 0;
        return switch (level) {
            case BRONZE   -> 5;
            case SILVER   -> 10;
            case GOLD     -> 15;
            case PLATINUM -> 22;
            case DIAMOND  -> 30;
        };
    }
}
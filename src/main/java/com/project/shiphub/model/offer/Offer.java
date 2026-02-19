package com.project.shiphub.model.offer;

import com.project.shiphub.model.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferType type = OfferType.PERCENTAGE;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferStatus status = OfferStatus.SCHEDULED;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToMany
    @JoinTable(
            name = "offer_products",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    @Column
    private String category;

    @Column
    private Integer stockLimit;

    @Column
    private Integer usedStock = 0;

    @Column
    private Integer priority = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return this.active
                && this.status == OfferStatus.ACTIVE
                && now.isAfter(startDate)
                && now.isBefore(endDate)
                && (stockLimit == null || usedStock < stockLimit);
    }

    public boolean shouldActivate() {
        LocalDateTime now = LocalDateTime.now();
        return this.active
                && this.status == OfferStatus.SCHEDULED
                && now.isAfter(startDate)
                && now.isBefore(endDate);
    }

    public boolean shouldDeactivate() {
        LocalDateTime now = LocalDateTime.now();
        return this.status == OfferStatus.ACTIVE
                && (now.isAfter(endDate) || (stockLimit != null && usedStock >= stockLimit));
    }

    public BigDecimal calculateDiscount(BigDecimal originalPrice) {
        if (type == OfferType.PERCENTAGE) {
            return originalPrice.multiply(discountValue).divide(new BigDecimal(100));
        }
        return discountValue;
    }

    public BigDecimal calculateFinalPrice(BigDecimal originalPrice) {
        BigDecimal discount = calculateDiscount(originalPrice);
        return originalPrice.subtract(discount);
    }
}
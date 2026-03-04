package com.project.shiphub.model.store;

import com.project.shiphub.model.auth.DropperProfile;
import com.project.shiphub.model.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dropper_stores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DropperStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "dropper_profile_id", nullable = false, unique = true)
    private DropperProfile dropperProfile;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(nullable = false, length = 100)
    private String storeName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String logoUrl;

    @Column(columnDefinition = "TEXT")
    private String bannerUrl;

    @Column(length = 7)
    @Builder.Default
    private String primaryColor = "#6366F1";

    @Column(length = 7)
    @Builder.Default
    private String secondaryColor = "#818CF8";

    @Column(length = 7)
    @Builder.Default
    private String backgroundColor = "#F8FAFC";

    @Column(length = 200)
    private String slogan;

    @Column(length = 20)
    @Builder.Default
    private String theme = "MODERN";

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = false;

    @ManyToMany
    @JoinTable(
            name = "store_products",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String aiPrompt;

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
}
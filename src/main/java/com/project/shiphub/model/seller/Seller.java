package com.project.shiphub.model.seller;

import com.project.shiphub.model.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "sellers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(unique = true, length = 100)
    private String stripeAccountId;

    @Enumerated(EnumType.STRING)
    private StripeAccountStatus stripeAccountStatus = StripeAccountStatus.NOT_CONNECTED;

    @Column(nullable = false)
    private Boolean chargesEnabled = false;

    @Column(nullable = false)
    private Boolean payoutsEnabled = false;

    @Column(nullable = false)
    private Integer commissionPercent = 70;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
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
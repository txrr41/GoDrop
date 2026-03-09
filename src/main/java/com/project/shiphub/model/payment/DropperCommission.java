package com.project.shiphub.model.payment;

import com.project.shiphub.model.auth.DropperProfile;
import com.project.shiphub.model.order.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dropper_commission")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class DropperCommission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dropper_profile_id", nullable = false)
    private DropperProfile dropperProfile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dropperMargin;

    @Column(precision = 10, scale = 2)
    private BigDecimal platformFee = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommissionStatus status;

    @Column
    private String stripeTransferId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum CommissionStatus {
        PENDING,
        PAID,
        FAILED,
        REFUNDED
    }
}
package com.project.shiphub.repository.payment;

import com.project.shiphub.model.payment.Payment;
import com.project.shiphub.model.payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByStripePaymentId(String stripePaymentId);

    List<Payment> findByOrderId(Long orderId);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByCreatedAtAfter(LocalDateTime date);

    List<Payment> findBySellerStripeAccountId(String accountId);

    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);
}

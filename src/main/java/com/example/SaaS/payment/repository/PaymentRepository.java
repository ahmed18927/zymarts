package com.example.SaaS.payment.repository;

import com.example.SaaS.common.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.orderId.id = :orderId")
    Optional<Payment> findByOrderId(Long orderId);

    @Query("SELECT p FROM Payment p WHERE p.gatewayPaymentId = :gatewayPaymentId")
    Optional<Payment> findByGatewayPaymentId(String gatewayPaymentId);

    @Query("SELECT p FROM Payment p WHERE p.customerId.id = :customerId")
    Page<Payment> findByCustomerId(Long customerId, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.companyId.id = :companyId")
    Page<Payment> findByCompanyId(Long companyId, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.status = :status")
    Page<Payment> findByStatus(String status, Pageable pageable);
}

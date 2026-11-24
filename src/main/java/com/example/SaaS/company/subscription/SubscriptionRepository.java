package com.example.SaaS.company.subscription;

import com.example.SaaS.common.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // إيجاد الاشتراك النشط للشركة
    @Query("SELECT s FROM Subscription s WHERE s.companyId.id = :companyId AND s.status = 'ACTIVE'")
    Optional<Subscription> findActiveSubscriptionByCompanyId(Long companyId);

    // إيجاد كل اشتراكات الشركة
    List<Subscription> findByCompanyIdId(Long companyId);

    // إيجاد الاشتراكات المنتهية
    @Query("SELECT s FROM Subscription s WHERE s.endDate < :currentDate AND s.status = 'ACTIVE'")
    List<Subscription> findExpiredSubscriptions(LocalDateTime currentDate);

    // إيجاد الاشتراكات التي ستنتهي قريباً
    @Query("SELECT s FROM Subscription s WHERE s.endDate BETWEEN :startDate AND :endDate AND s.status = 'ACTIVE'")
    List<Subscription> findExpiringSubscriptions(LocalDateTime startDate, LocalDateTime endDate);
}

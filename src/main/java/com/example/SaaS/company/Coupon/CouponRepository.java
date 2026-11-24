package com.example.SaaS.company.Coupon;

import com.example.SaaS.common.entity.Banner;
import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<Coupon,Long> {
    Page<Coupon> findByCompanyId(Long companyId ,   Pageable pageable);
//    @Query("""
//            SELECT b FROM Coupon b
//            WHERE b.companyId.id = :companyId
//            """)
//    Page<Coupon> findByCompanyId(
//            @Param("companyId") Long companyId,
//            Pageable pageable
//    );

    Optional<Coupon> findByIdAndCompanyId(Long id, Long companyId);
    @Query("""
        SELECT c FROM Coupon c
        WHERE c.isActive = true
        AND c.startDate <= CURRENT_DATE
        AND c.endDate >= CURRENT_DATE
        AND (c.maxUsed IS NULL OR c.used < c.maxUsed)
        ORDER BY c.id DESC
    """)
    Page<Coupon> findValidCoupons(Pageable pageable);
    Optional<Coupon> findByCodeIgnoreCaseAndCompanyId_Id(String code, Long companyId);
}


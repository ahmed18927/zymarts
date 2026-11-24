package com.example.SaaS.seeder;

import com.example.SaaS.common.entity.Coupon;
import com.example.SaaS.company.Coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Order(4)
@Component
@RequiredArgsConstructor
public class CouponSeeder implements CommandLineRunner {
    private final CouponRepository couponRepository;

    @Override
    public void run(String... args) {

        if (couponRepository.count() > 0) return; // prevent duplicates

        // ===== Sample Coupons =====
        Coupon percentageCoupon = createCoupon(
                "SUMMER20",
                "percentage",
                BigDecimal.valueOf(20),
                100,
                true,
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                false,
                5,
                true
        );

        Coupon valueCoupon = createCoupon(
                "WELCOME50",
                "value",
                BigDecimal.valueOf(50),
                50,
                true,
                LocalDate.now(),
                LocalDate.now().plusMonths(2),
                true,
                1,
                true
        );

        couponRepository.saveAll(List.of(percentageCoupon, valueCoupon));
    }

    // ===== Helper =====
    private Coupon createCoupon(
            String code,
            String discountType,
            BigDecimal discountValue,
            Integer maxUsed,
            Boolean isActive,
            LocalDate startDate,
            LocalDate endDate,
            Boolean isFreeShipping,
            Integer maxUsePerUser,
            Boolean appliedOnDiscount
    ) {
        Coupon coupon = new Coupon();
        coupon.setCode(code);
        coupon.setDiscountType(discountType);
        coupon.setDiscountValue(discountValue);
        coupon.setMaxUsed(maxUsed);
        coupon.setIsActive(isActive);
        coupon.setStartDate(startDate);
        coupon.setEndDate(endDate);
        coupon.setIsFreeShipping(isFreeShipping);
        coupon.setMaxUsePerUser(maxUsePerUser);
        coupon.setAppliedOnDiscount(appliedOnDiscount);

        // Optional: set a default image if you want
        // coupon.setImagePath("/images/coupons/default.png");

        return coupon;
    }
}

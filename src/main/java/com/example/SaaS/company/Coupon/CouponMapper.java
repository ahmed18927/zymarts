package com.example.SaaS.company.Coupon;

import com.example.SaaS.common.entity.Coupon;

import lombok.Builder;
import org.springframework.stereotype.Component;
@Builder
@Component
public class CouponMapper {
    public CouponResourceDTO toResponse(
            Coupon coupon
    ){
        return CouponResourceDTO.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
//                .maxUsed(coupon.getMaxUsed())
                .used(coupon.getUsed())
//                .startDate(coupon.getStartDate())
//                .endDate(coupon.getEndDate())
                .startDate(coupon.getStartDate().atStartOfDay())
                .endDate(coupon.getEndDate().atStartOfDay())
                .image(coupon.getImage())
                .isFreeShipping(coupon.getIsFreeShipping())
                .maxUsePerUser(coupon.getMaxUsePerUser())
                .appliedOnDiscount(coupon.getAppliedOnDiscount())
                .build();
    }



}

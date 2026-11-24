package com.example.SaaS.superAdmin.coupon;

import com.example.SaaS.common.entity.Coupon;
import com.example.SaaS.superAdmin.coupon.dto.AdminCouponResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AdminCouponMapper {
    public AdminCouponResponseDTO toResponse(Coupon coupon) {
        return AdminCouponResponseDTO.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .maxUsed(coupon.getMaxUsed())
                .used(coupon.getUsed())
                .isActive(coupon.getIsActive())
                .startDate(coupon.getStartDate() != null ? coupon.getStartDate().atStartOfDay() : null)
                .endDate(coupon.getEndDate() != null ? coupon.getEndDate().atStartOfDay() : null)
                .image(coupon.getImage())
                .isFreeShipping(coupon.getIsFreeShipping())
                .maxUsePerUser(coupon.getMaxUsePerUser())
                .appliedOnDiscount(coupon.getAppliedOnDiscount())
                .companyId(coupon.getCompany() != null ? coupon.getCompany().getId() : null)
                .companyName(coupon.getCompany() != null ? coupon.getCompany().getName() : null)
                .build();
    }
}

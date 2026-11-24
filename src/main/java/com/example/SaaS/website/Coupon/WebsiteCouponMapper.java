package com.example.SaaS.website.Coupon;

import com.example.SaaS.common.entity.Coupon;
import org.springframework.stereotype.*;

@Component
public class WebsiteCouponMapper {

    public CouponDto toDto(Coupon c) {

        if (c == null) return null;

        CouponDto dto = new CouponDto();
        dto.setId(c.getId());
        dto.setCode(c.getCode());
        dto.setDiscountType(c.getDiscountType());
        dto.setDiscountValue(c.getDiscountValue());
        dto.setIsFreeShipping(c.getIsFreeShipping());

        return dto;
    }
}

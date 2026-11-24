package com.example.SaaS.website.Coupon;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CouponDto {

    private Long id;
    private String code;
    private String discountType;
    private BigDecimal discountValue;
    private Boolean isFreeShipping;
}

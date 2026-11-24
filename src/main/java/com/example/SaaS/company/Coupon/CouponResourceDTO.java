package com.example.SaaS.company.Coupon;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CouponResourceDTO {
    private Long id;
    private String code;
    private String discountType;
    private BigDecimal discountValue;
    private Integer used;
    private Boolean isActive;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String image;
    private Boolean isFreeShipping;
    private Integer maxUsePerUser;
    private Boolean appliedOnDiscount;

}

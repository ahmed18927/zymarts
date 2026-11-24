package com.example.SaaS.superAdmin.coupon.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminCouponResponseDTO {
    private Long id;
    private String code;
    private String discountType;
    private BigDecimal discountValue;
    private Integer maxUsed;
    private Integer used;
    private Boolean isActive;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String image;
    private Boolean isFreeShipping;
    private Integer maxUsePerUser;
    private Boolean appliedOnDiscount;
    private Long companyId;
    private String companyName;
}

package com.example.SaaS.superAdmin.coupon.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AdminCouponRequest {
    @NotBlank(message = "Coupon code is required")
    @Size(max = 50, message = "Coupon code must not exceed 50 characters")
    private String code;

    @NotBlank(message = "Discount type is required")
    private String discountType;

    @NotNull(message = "Discount value is required")
    private BigDecimal discountValue;

    @NotNull(message = "Max used is required")
    @Min(value = 1, message = "Max used must be at least 1")
        private Integer maxUsed;

    private Boolean isActive;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @FutureOrPresent(message = "End date must be today or in the future")
    private LocalDate endDate;

    private MultipartFile image;

    @NotNull(message = "Free shipping is required")
    private Boolean isFreeShipping;

    @Min(value = 1, message = "Max use per user must be at least 1")
    private Integer maxUsePerUser;

    @NotNull(message = "Applied on discount is required")
    private Boolean appliedOnDiscount;

    private Long companyId;
}

package com.example.SaaS.company.Coupon;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CouponRequest {
    @NotBlank(message = "Coupon code is required")
    @Size(max = 50, message = "Coupon code must not exceed 50 characters")
//    @UniqueCouponCode
    private String code;
    @NotBlank
    private String discountType;
    @NotNull
    private BigDecimal discountValue;
    @NotNull(message = "max used is required")
    @Min(value = 1, message = "Max used must be at least 1")
    private Integer maxUsed;
    @NotNull(message = "Status is required")
    @Min(value = 0, message = "Status must be 0 or 1")
    @Max(value = 1, message = "Status must be 0 or 1")
    private  Boolean isActive;
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;
    @FutureOrPresent(message = "End date must be today or in the future")

    private LocalDate  endDate;

    private MultipartFile image;
    @NotNull(message = "free shipping is required")
    @Min(value = 0, message = "free shipping must be 0 or 1")
    @Max(value = 1, message = "free shipping must be 0 or 1")
    private Boolean isFreeShipping;
    @Min(value = 1, message = "Max use per user must be at least 1")
    private Integer maxUsePerUser;
    @NotNull(message = "Applied on discount is required")
    @Min(0)
    @Max(1)
    private Boolean appliedOnDiscount;

}

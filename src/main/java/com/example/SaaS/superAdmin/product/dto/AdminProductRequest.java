package com.example.SaaS.superAdmin.product.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminProductRequest {

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotBlank(message = "Arabic name is required")
    @Size(min = 2, max = 255, message = "Arabic name must be between 2 and 255 characters")
    private String nameAr;

    @NotBlank(message = "English name is required")
    @Size(min = 2, max = 255, message = "English name must be between 2 and 255 characters")
    private String nameEn;

    @NotNull(message = "Sell price is required")
    @DecimalMin(value = "0.01", message = "Sell price must be at least 0.01")
    @DecimalMax(value = "99999999.99", message = "Sell price cannot exceed 99,999,999.99")
    @Digits(integer = 8, fraction = 2, message = "Sell price must have maximum 8 digits before decimal and 2 after")
    private BigDecimal sellPrice;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    @Max(value = 2147483647, message = "Stock cannot exceed 2,147,483,647")
    private Integer stock;

    private Long categoryId;
    private Long subCategoryId;
    private Long storeId;

    @DecimalMin(value = "0.0", message = "Buying price cannot be negative")
    @DecimalMax(value = "99999999.99", message = "Buying price cannot exceed 99,999,999.99")
    @Digits(integer = 8, fraction = 2, message = "Buying price must have maximum 8 digits before decimal and 2 after")
    private BigDecimal buyingPrice;

    private Boolean isActive = true;

    @Min(value = 0, message = "Ordering cannot be negative")
    @Max(value = 2147483647, message = "Ordering cannot exceed 2,147,483,647")
    private Integer ordering = 0;

    @Size(max = 5000, message = "Arabic description cannot exceed 5000 characters")
    private String descAr;

    @Size(max = 5000, message = "English description cannot exceed 5000 characters")
    private String descEn;

    @Size(max = 5000, message = "Arabic specifications cannot exceed 5000 characters")
    private String specificationsAr;

    @Size(max = 5000, message = "English specifications cannot exceed 5000 characters")
    private String specificationsEn;

    @Size(min = 1, max = 100, message = "SKU must be between 1 and 100 characters")
    private String sku;

    @Size(max = 100, message = "MPN must not exceed 100 characters")
    private String mpn;

    @Pattern(regexp = "PERCENTAGE|FIXED|NONE", message = "Discount type must be PERCENTAGE, FIXED, or NONE")
    private String discountType;

    @DecimalMin(value = "0.0", message = "Discount value cannot be negative")
    @DecimalMax(value = "99999999.99", message = "Discount value cannot exceed 99,999,999.99")
    @Digits(integer = 8, fraction = 2, message = "Discount value must have maximum 8 digits before decimal and 2 after")
    private BigDecimal discountValue;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

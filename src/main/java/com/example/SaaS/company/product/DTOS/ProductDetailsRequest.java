package com.example.SaaS.company.product.DTOS;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class ProductDetailsRequest {

    @NotBlank(message = "SKU is required")
    @Size(min = 1, max = 100, message = "SKU must be between 1 and 100 characters")
    private String sku;

    @Size(max = 100, message = "MPN must not exceed 100 characters")
    private String mpn;

    @Pattern(regexp = "PERCENTAGE|FIXED|NONE", message = "Discount type must be PERCENTAGE, FIXED, or NONE")
    private String discountType;

    @DecimalMin(value = "0.0", message = "Discount value cannot be negative")
    @DecimalMax(value = "99999999.99", message = "Discount value cannot exceed 99,999,999.99") // ⬅️ أضف ده
    @Digits(integer = 8, fraction = 2, message = "Discount value must have maximum 8 digits before decimal and 2 after")
    private BigDecimal discountValue;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // ⬅️ أضف validation method
    @AssertTrue(message = "End date must be after start date")
    private boolean isEndDateValid() {
        if (startDate == null || endDate == null) {
            return true; // Skip validation if either is null
        }
        return endDate.isAfter(startDate);
    }
}

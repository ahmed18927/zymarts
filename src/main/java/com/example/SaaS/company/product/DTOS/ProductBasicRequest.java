package com.example.SaaS.company.product.DTOS;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductBasicRequest {

    @NotNull(message = "Sell price is required")
    @DecimalMin(value = "0.01", message = "Sell price must be at least 0.01")
    @DecimalMax(value = "99999999.99", message = "Sell price cannot exceed 99,999,999.99") // ⬅️ أضف ده
    @Digits(integer = 8, fraction = 2, message = "Sell price must have maximum 8 digits before decimal and 2 after")
    private BigDecimal sellPrice;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    @Max(value = 2147483647, message = "Stock cannot exceed 2,147,483,647") // ⬅️ أضف ده
    private Integer stock;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @Min(value = 0, message = "Ordering cannot be negative")
    @Max(value = 2147483647, message = "Ordering cannot exceed 2,147,483,647")
    private Integer ordering;

    @DecimalMin(value = "0.0", message = "Buying price cannot be negative")
    @DecimalMax(value = "99999999.99", message = "Buying price cannot exceed 99,999,999.99") // ⬅️ أضف ده
    @Digits(integer = 8, fraction = 2, message = "Buying price must have maximum 8 digits before decimal and 2 after")
    private BigDecimal buyingPrice;

    private Long companyId;

    // Arabic translations
    @NotBlank(message = "Arabic name is required")
    @Size(min = 2, max = 255, message = "Arabic name must be between 2 and 255 characters")
    private String nameAr;

    @Size(max = 5000, message = "Arabic description cannot exceed 5000 characters")
    private String descAr;

    @Size(max = 5000, message = "Arabic specifications cannot exceed 5000 characters")
    private String specificationsAr;

    // English translations
    @NotBlank(message = "English name is required")
    @Size(min = 2, max = 255, message = "English name must be between 2 and 255 characters")
    private String nameEn;

    @Size(max = 5000, message = "English description cannot exceed 5000 characters")
    private String descEn;

    @Size(max = 5000, message = "English specifications cannot exceed 5000 characters")
    private String specificationsEn;
}


package com.example.SaaS.superAdmin.packagee;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
public class PackageRequest {
    @NotBlank(message = "English name is required")
    @Size(max = 255, message = "English name must not exceed 255 characters")
    private String nameEn;
    @NotBlank(message = "Arabic name is required")
    @Size(max = 255, message = "Arabic name must not exceed 255 characters")
    private String nameAr;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be zero or greater")
    private BigDecimal price;
    @Min(value = 1, message = "Max products must be at least 1")
    private Integer maxProducts;
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1")
    private Integer duration;
    @Min(value = 1, message = "Max orders must be at least 1")
    private  Integer maxOrders;
    @NotNull(message = "Ordering is required")
    @Min(value = 1, message = "Ordering must be at least 1")

    private  Integer maxOrder;
    @NotNull
    private Integer ordering;
    @Size(max = 1000, message = "English description is too long")
    private String descriptionEn;
    @Size(max = 1000, message = "Arabic description is too long")
    private String descriptionAr;
//    @Min(value = 0, message = "Status must be 0 or 1")
//    @Max(value = 1, message = "Status must be 0 or 1")
     private  Boolean isActive;

}

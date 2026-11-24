package com.example.SaaS.superAdmin.country;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CountryRequest {
    @NotBlank(message = "Arabic name is required")
    @Size(max = 255, message = "Arabic name must not exceed 255 characters")
    private String nameAr;

    @NotBlank(message = "English name is required")
    @Size(max = 255, message = "English name must not exceed 255 characters")
    private String nameEn;

    @NotBlank(message = "code is required")
    @Size(min = 2, max = 3 , message = "Code must be between 2 and 3 characters")
    private String code;
}

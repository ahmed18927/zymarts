package com.example.SaaS.superAdmin.feature;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class FeatureRequest {
    @NotBlank(message = "English name is required")
    @Size(max = 255, message = "English name must not exceed 255 characters")
    private String nameEn;
    @NotBlank(message = "Arabic name is required")
    @Size(max = 255, message = "Arabic name must not exceed 255 characters")
    private String nameAr;
    @NotNull(message = "Ordering is required")
    @Min(value = 1, message = "Ordering must be at least 1")
    private Integer ordering;
    @Size(max = 1000, message = "English description is too long")
    private String descriptionEn;
    @Size(max = 1000, message = "Arabic description is too long")
    private String descriptionAr;
    @NotNull(message = "Status is required")
//    @Min(value = 0, message = "Status must be 0 or 1")
//    @Max(value = 1, message = "Status must be 0 or 1")
    private  Boolean isActive;
    @NotBlank(message = "Slug is required")
    @Pattern(
            regexp = "^[a-z0-9-]+$",
            message = "Slug must contain only lowercase letters, numbers, and hyphens"
    )
    private String slug;
    @NotBlank(message = "Type is required")

    private String type;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;
    @FutureOrPresent(message = "End date must be today or in the future")

    private LocalDate  endDate;
//    @NotNull(message = "Image is required")

    private MultipartFile image;
}

package com.example.SaaS.company.category;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryRequest {
    @NotBlank(message = "Slug is required")
    @Pattern(
            regexp = "^[a-z0-9-]+$",
            message = "Slug must contain only lowercase letters, numbers, and hyphens"
    )
    private String slug;
    @NotBlank(message = "English name is required")
    @Size(max = 255, message = "English name must not exceed 255 characters")
    private String nameEn;
    @NotBlank(message = "Arabic name is required")
    @Size(max = 255, message = "Arabic name must not exceed 255 characters")
    private String nameAr;

    @Size(max = 1000, message = "English description is too long")
    private String descriptionEn;
    @Size(max = 1000, message = "Arabic description is too long")
    private String descriptionAr;
    @NotNull(message = "Status is required")
//    @Min(value = 0, message = "Status must be 0 or 1")
//    @Max(value = 1, message = "Status must be 0 or 1")
    private  Boolean   isActive;
    @NotNull(message = "Ordering is required")
    @Min(value = 1, message = "Ordering must be at least 1")
    private Integer ordering;
    // For image upload
    private MultipartFile image;

}

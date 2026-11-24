package com.example.SaaS.company.subCategory;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SubCategoryRequest {
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
    @NotBlank(message = "Slug is required")
    @Pattern(
            regexp = "^[a-z0-9-]+$",
            message = "Slug must contain only lowercase letters, numbers, and hyphens"
    )
    private String slug;
    @NotNull(message = "Status is required")
//    @Min(value = 0, message = "Status must be 0 or 1")
//    @Max(value = 1, message = "Status must be 0 or 1")
    private Boolean isActive;
    @NotNull(message = "Ordering is required")
    @Min(value = 1, message = "Ordering must be at least 1")
    private Integer ordering;
    private Long categoryId;
    private Long parentId;


}

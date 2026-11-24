package com.example.SaaS.superAdmin.banner.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class AdminBannerRequest {
    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotNull(message = "Ordering is required")
    @Min(value = 1, message = "Ordering must be at least 1")
    private Integer ordering;

    private Boolean isActive;

    @Size(max = 255, message = "Link must not exceed 255 characters")
    private String link;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Category is required")
    private Long categoryId;

    private MultipartFile image;
}

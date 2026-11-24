package com.example.SaaS.company.banner;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class BannerRequest {
    @NotNull(message = "Ordering is required")
    @Min(value = 1, message = "Ordering must be at least 1")
    private Integer ordering;
    @NotNull(message = "Status is required")
    @Min(value = 0, message = "Status must be 0 or 1")
    @Max(value = 1, message = "Status must be 0 or 1")
    private Boolean isActive;
    @Size(max = 255, message = "Link must not exceed 255 characters")
    @Pattern(
            regexp = "^(https?://).+",
            message = "Link must be a valid URL"
    )
    private String link;
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;
    @FutureOrPresent(message = "End date must be today or in the future")
    private LocalDate  endDate;

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotNull(message = "Image is required")
    private MultipartFile image;

}

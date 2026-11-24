package com.example.SaaS.company.DTOS;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegistrationRequest {

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String companyName;

    @NotBlank(message = "Domain is required")
    @Size(min = 3, max = 255, message = "Domain must be between 3 and 255 characters")
    @Pattern(
            regexp = "^[a-z0-9]([a-z0-9-]{0,61}[a-z0-9])?(\\.[a-z0-9]([a-z0-9-]{0,61}[a-z0-9])?)*$",
            message = "Invalid domain format (e.g., example.com)"
    )
    private String domain;

    private MultipartFile companyLogo;

    // =================== بيانات الموظف الرئيسي ===================
    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 100, message = "Employee name must be between 2 and 100 characters")
    private String employeeName;

    @NotBlank(message = "Employee email is required")
    @Email(message = "Invalid email format")
    private String employeeEmail;

    @NotBlank(message = "Employee phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String employeePhone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private MultipartFile employeeImage;

    // =================== بيانات الباكدج والاشتراك ===================
    @NotNull(message = "Package ID is required")
    @Positive(message = "Package ID must be positive")
    private Long packageId;

    @NotNull(message = "Subscription duration is required")
    @Min(value = 1, message = "Duration must be at least 1 month")
    @Max(value = 12, message = "Duration must not exceed 12 months")
    private Integer durationMonths; // عدد الشهور للاشتراك
}
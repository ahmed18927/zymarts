package com.example.SaaS.superAdmin.employee.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AdminEmployeeRequest {
    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{8,15}$", message = "Phone number must be between 8 and 15 digits")
    private String phone;
}

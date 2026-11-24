package com.example.SaaS.company.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EmployeeRequest  {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between {min} and {max} characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 150, message = "Email must not exceed 150 characters")
//    @UniqueEmployeeEmail   // optional (Laravel unique)
    private String email;
    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Password must contain letters and numbers"
    )
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{8,15}$",
            message = "Phone number must be between 8 and 15 digits"
    )
//    @UniqueEmployeePhone   // optional
    private String phone;

    private MultipartFile image;

}

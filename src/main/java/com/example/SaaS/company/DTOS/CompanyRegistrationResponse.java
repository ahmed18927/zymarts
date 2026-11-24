package com.example.SaaS.company.DTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegistrationResponse {
    // =================== بيانات الشركة ===================
    private Long companyId;
    private String companyName;
    private String domain;
    private String companyLogo;
    private Boolean isActive;

    // =================== بيانات الموظف الرئيسي ===================
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private String employeePhone;
    private String employeeImage;
    private Boolean isMainEmployee;

    // =================== بيانات الاشتراك ===================
    private Long subscriptionId;
    private Long packageId;
    private String packageName;
    private LocalDateTime subscriptionStartDate;
    private LocalDateTime subscriptionEndDate;
    private String subscriptionStatus;

    private Instant createdAt;
    private String message;
}
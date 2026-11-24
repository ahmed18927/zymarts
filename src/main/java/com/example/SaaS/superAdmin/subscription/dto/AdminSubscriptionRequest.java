package com.example.SaaS.superAdmin.subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminSubscriptionRequest {

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotNull(message = "Package ID is required")
    private Long packageId;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    private String status;

    private String packageData;
}

package com.example.SaaS.company.subscription.DTOS;

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
public class SubscriptionRequest {

    @NotNull(message = "Company ID is required")
    private Long companyId;

    @NotNull(message = "Package ID is required")
    private Long packageId;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    private String status; // ACTIVE, EXPIRED, CANCELLED

    private String packageData; // JSON data for package details
}

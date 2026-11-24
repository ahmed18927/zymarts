package com.example.SaaS.company.subscription.DTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionResponse {
private Long id;
private Long companyId;
private String companyName;
private Long packageId;
private String packageName;
private LocalDateTime startDate;
private LocalDateTime endDate;
private String status;
private String packageData;
private Instant createdAt;
private Instant updatedAt;
private Long daysRemaining; // عدد الأيام المتبقية
private Boolean isExpired; // هل انتهى الاشتراك
}
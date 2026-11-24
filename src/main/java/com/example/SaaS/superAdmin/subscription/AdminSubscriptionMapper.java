package com.example.SaaS.superAdmin.subscription;

import com.example.SaaS.common.entity.Subscription;
import com.example.SaaS.superAdmin.subscription.dto.AdminSubscriptionResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class AdminSubscriptionMapper {

    public AdminSubscriptionResponse toResponse(Subscription subscription) {
        if (subscription == null) {
            return null;
        }

        Long daysRemaining = null;
        Boolean isExpired = false;

        if (subscription.getEndDate() != null) {
            LocalDateTime now = LocalDateTime.now();
            daysRemaining = ChronoUnit.DAYS.between(now, subscription.getEndDate());
            isExpired = now.isAfter(subscription.getEndDate());
        }

        return AdminSubscriptionResponse.builder()
                .id(subscription.getId())
                .companyId(subscription.getCompanyId() != null ? subscription.getCompanyId().getId() : null)
                .companyName(subscription.getCompanyId() != null ? subscription.getCompanyId().getName() : null)
                .packageId(subscription.getPackageId() != null ? subscription.getPackageId().getId() : null)
                .packageName(subscription.getPackageId() != null ? subscription.getPackageId().getTranslations().get(0).getName() : null)
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .status(subscription.getStatus())
                .packageData(subscription.getPackageData())
                .createdAt(subscription.getCreatedAt())
                .updatedAt(subscription.getUpdatedAt())
                .daysRemaining(daysRemaining)
                .isExpired(isExpired)
                .build();
    }
}

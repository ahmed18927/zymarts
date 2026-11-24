package com.example.SaaS.superAdmin.subscription;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Package;
import com.example.SaaS.common.entity.Subscription;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.subscription.SubscriptionRepository;
import com.example.SaaS.superAdmin.subscription.dto.AdminSubscriptionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminSubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public Page<Subscription> getAllSubscriptions(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return subscriptionRepository.findAll(pageable);
        }
        return subscriptionRepository.findAll(pageable);
    }

    public Subscription getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Subscription not found"));
    }

    @Transactional
    public Subscription createSubscription(AdminSubscriptionRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessException("End date must be after start date");
        }

        Subscription subscription = Subscription.builder()
                .companyId(Company.builder().id(request.getCompanyId()).build())
                .packageId(Package.builder().id(request.getPackageId()).build())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .packageData(request.getPackageData())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public Subscription updateSubscription(Long id, AdminSubscriptionRequest request) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Subscription not found"));

        if (request.getCompanyId() != null) {
            subscription.setCompanyId(Company.builder().id(request.getCompanyId()).build());
        }

        if (request.getPackageId() != null) {
            subscription.setPackageId(Package.builder().id(request.getPackageId()).build());
        }

        if (request.getStartDate() != null) {
            subscription.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            subscription.setEndDate(request.getEndDate());
        }

        if (request.getStatus() != null) {
            subscription.setStatus(request.getStatus());
        }

        if (request.getPackageData() != null) {
            subscription.setPackageData(request.getPackageData());
        }

        subscription.setUpdatedAt(Instant.now());

        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public void deleteSubscription(Long id) {
        Subscription subscription = getSubscriptionById(id);
        subscriptionRepository.delete(subscription);
    }

    @Transactional
    public Subscription updateStatus(Long id, String newStatus) {
        Subscription subscription = getSubscriptionById(id);

        if (!java.util.List.of("ACTIVE", "EXPIRED", "CANCELLED").contains(newStatus)) {
            throw new BusinessException("Invalid status. Must be ACTIVE, EXPIRED, or CANCELLED");
        }

        subscription.setStatus(newStatus);
        subscription.setUpdatedAt(Instant.now());

        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public Subscription renewSubscription(Long id, LocalDateTime newEndDate) {
        Subscription subscription = getSubscriptionById(id);

        if (newEndDate.isBefore(subscription.getEndDate())) {
            throw new BusinessException("New end date must be after current end date");
        }

        subscription.setEndDate(newEndDate);
        subscription.setStatus("ACTIVE");
        subscription.setUpdatedAt(Instant.now());

        return subscriptionRepository.save(subscription);
    }
}

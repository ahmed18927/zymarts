package com.example.SaaS.company.subscription;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Subscription;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.subscription.DTOS.SubscriptionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.SaaS.common.entity.Package;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public Subscription create(SubscriptionRequest request){

        subscriptionRepository.findActiveSubscriptionByCompanyId(request.getCompanyId())
                .ifPresent( existing -> {
                    throw new BusinessException("Company already has an active subscription");
                });

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessException("End date must be after start date");
        }

        Subscription subscription = Subscription.builder()
                .companyId(Company.builder().id(request.getCompanyId()).build())
                .packageId(com.example.SaaS.common.entity.Package.builder().id(request.getPackageId()).build())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status("ACTIVE")
                .packageData(request.getPackageData())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public Subscription renew(Long subscriptionId, LocalDateTime newEndDate) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new BusinessException("Subscription not found"));

        if (newEndDate.isBefore(subscription.getEndDate())) {
            throw new BusinessException("New end date must be after current end date");
        }

        subscription.setEndDate(newEndDate);
        subscription.setStatus("ACTIVE");
        subscription.setUpdatedAt(Instant.now());

        return subscriptionRepository.save(subscription);
    }

    /**
     * تغيير الباقة (Change Package)
     */
    @Transactional
    public Subscription changePackage(Long subscriptionId, Long newPackageId, String newPackageData) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new BusinessException("Subscription not found"));

        if (!"ACTIVE".equals(subscription.getStatus())) {
            throw new BusinessException("Can only change package for active subscriptions");
        }

        subscription.setPackageId(Package.builder().id(newPackageId).build());
        subscription.setPackageData(newPackageData);
        subscription.setUpdatedAt(Instant.now());

        return subscriptionRepository.save(subscription);
    }

    /**
     * إلغاء الاشتراك (Cancel)
     */
    @Transactional
    public Subscription cancel(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new BusinessException("Subscription not found"));

        subscription.setStatus("CANCELLED");
        subscription.setUpdatedAt(Instant.now());

        return subscriptionRepository.save(subscription);
    }

    /**
     * تحديث حالة الاشتراك يدوياً
     */
    @Transactional
    public Subscription updateStatus(Long subscriptionId, String newStatus) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new BusinessException("Subscription not found"));

        if (!List.of("ACTIVE", "EXPIRED", "CANCELLED").contains(newStatus)) {
            throw new BusinessException("Invalid status. Must be ACTIVE, EXPIRED, or CANCELLED");
        }

        subscription.setStatus(newStatus);
        subscription.setUpdatedAt(Instant.now());

        return subscriptionRepository.save(subscription);
    }

    /**
     * جدولة تلقائية لتحديث الاشتراكات المنتهية
     * تعمل كل ساعة
     */
    @Scheduled(fixedRate = 3600000) // كل ساعة
    @Transactional
    public void autoExpireSubscriptions() {
        LocalDateTime now = LocalDateTime.now();
        List<Subscription> expiredSubscriptions = subscriptionRepository.findExpiredSubscriptions(now);

        for (Subscription subscription : expiredSubscriptions) {
            subscription.setStatus("EXPIRED");
            subscription.setUpdatedAt(Instant.now());
            subscriptionRepository.save(subscription);
        }

        if (!expiredSubscriptions.isEmpty()) {
            System.out.println("Auto-expired " + expiredSubscriptions.size() + " subscriptions");
        }
    }

    /**
     * الحصول على اشتراك بواسطة ID
     */
    public Subscription getById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Subscription not found"));
    }

    /**
     * الحصول على الاشتراك النشط للشركة
     */
    public Subscription getActiveSubscriptionByCompanyId(Long companyId) {
        return subscriptionRepository.findActiveSubscriptionByCompanyId(companyId)
                .orElseThrow(() -> new BusinessException("No active subscription found for this company"));
    }

    /**
     * الحصول على جميع اشتراكات الشركة
     */
    public List<Subscription> getAllByCompanyId(Long companyId) {
        return subscriptionRepository.findByCompanyIdId(companyId);
    }

    /**
     * الحصول على جميع الاشتراكات
     */
    public Page<Subscription> getAllSubscriptions(Pageable pageable) {
        return subscriptionRepository.findAll(pageable);
    }

    /**
     * الحصول على الاشتراكات التي ستنتهي خلال عدد معين من الأيام
     */
    public List<Subscription> getExpiringSubscriptions(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusDays(days);
        return subscriptionRepository.findExpiringSubscriptions(now, futureDate);
    }

    /**
     * حذف اشتراك
     */
    @Transactional
    public void delete(Long id) {
        Subscription subscription = getById(id);
        subscriptionRepository.delete(subscription);
    }
}

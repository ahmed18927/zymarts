package com.example.SaaS.company.subscription;

import com.example.SaaS.common.entity.Subscription;
import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.company.subscription.DTOS.SubscriptionRequest;
import com.example.SaaS.company.subscription.DTOS.SubscriptionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    /**
     * إنشاء اشتراك جديد
     * POST /api/subscriptions
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SubscriptionResponse>> create(
            @Valid @RequestBody SubscriptionRequest request
    ) {
        Subscription created = subscriptionService.create(request);
        SubscriptionResponse response = subscriptionMapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Subscription created successfully", response));
    }

    /**
     * الحصول على جميع الاشتراكات
     * GET /api/subscriptions
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SubscriptionResponse>>> getAll(
            @PageableDefault(size = 10) Pageable pageable) {

        Page<SubscriptionResponse> response = subscriptionService.getAllSubscriptions(pageable)
                .map(subscriptionMapper::toResponse);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Subscriptions retrieved successfully", response)
        );
    }

    /**
     * الحصول على اشتراك بواسطة ID
     * GET /api/subscriptions/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> getById(@PathVariable Long id) {
        Subscription subscription = subscriptionService.getById(id);
        SubscriptionResponse response = subscriptionMapper.toResponse(subscription);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Subscription retrieved successfully", response)
        );
    }

    /**
     * الحصول على الاشتراك النشط للشركة
     * GET /api/subscriptions/company/{companyId}/active
     */
    @GetMapping("/company/{companyId}/active")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> getActiveByCompanyId(
            @PathVariable Long companyId
    ) {
        Subscription subscription = subscriptionService.getActiveSubscriptionByCompanyId(companyId);
        SubscriptionResponse response = subscriptionMapper.toResponse(subscription);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Active subscription retrieved successfully", response)
        );
    }

    /**
     * الحصول على جميع اشتراكات الشركة
     * GET /api/subscriptions/company/{companyId}
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<SubscriptionResponse>>> getAllByCompanyId(
            @PathVariable Long companyId
    ) {
        List<SubscriptionResponse> response = subscriptionService.getAllByCompanyId(companyId)
                .stream()
                .map(subscriptionMapper::toResponse)
                .toList();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company subscriptions retrieved successfully", response)
        );
    }

    /**
     * تجديد الاشتراك
     * PUT /api/subscriptions/{id}/renew
     */
    @PutMapping("/{id}/renew")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> renew(
            @PathVariable Long id,
            @RequestParam LocalDateTime newEndDate
    ) {
        Subscription renewed = subscriptionService.renew(id, newEndDate);
        SubscriptionResponse response = subscriptionMapper.toResponse(renewed);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Subscription renewed successfully", response)
        );
    }

    /**
     * تغيير الباقة
     * PUT /api/subscriptions/{id}/change-package
     */
    @PutMapping("/{id}/change-package")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> changePackage(
            @PathVariable Long id,
            @RequestParam Long newPackageId,
            @RequestParam(required = false) String newPackageData
    ) {
        Subscription updated = subscriptionService.changePackage(id, newPackageId, newPackageData);
        SubscriptionResponse response = subscriptionMapper.toResponse(updated);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Package changed successfully", response)
        );
    }

    /**
     * إلغاء الاشتراك
     * PUT /api/subscriptions/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> cancel(@PathVariable Long id) {
        Subscription cancelled = subscriptionService.cancel(id);
        SubscriptionResponse response = subscriptionMapper.toResponse(cancelled);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Subscription cancelled successfully", response)
        );
    }

    /**
     * تحديث حالة الاشتراك
     * PUT /api/subscriptions/{id}/status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        Subscription updated = subscriptionService.updateStatus(id, status);
        SubscriptionResponse response = subscriptionMapper.toResponse(updated);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Subscription status updated successfully", response)
        );
    }

    /**
     * الحصول على الاشتراكات التي ستنتهي خلال عدد معين من الأيام
     * GET /api/subscriptions/expiring?days=7
     */
    @GetMapping("/expiring")
    public ResponseEntity<ApiResponse<List<SubscriptionResponse>>> getExpiringSubscriptions(
            @RequestParam(defaultValue = "7") int days
    ) {
        List<SubscriptionResponse> response = subscriptionService.getExpiringSubscriptions(days)
                .stream()
                .map(subscriptionMapper::toResponse)
                .toList();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Expiring subscriptions retrieved successfully", response)
        );
    }

    /**
     * حذف اشتراك
     * DELETE /api/subscriptions/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        subscriptionService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Subscription deleted successfully", null)
        );
    }
}
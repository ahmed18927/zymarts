package com.example.SaaS.superAdmin.subscription;

import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.common.entity.Subscription;
import com.example.SaaS.superAdmin.subscription.dto.AdminSubscriptionRequest;
import com.example.SaaS.superAdmin.subscription.dto.AdminSubscriptionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/admin/subscriptions")
@RequiredArgsConstructor
public class AdminSubscriptionController {

    private final AdminSubscriptionService adminSubscriptionService;
    private final AdminSubscriptionMapper adminSubscriptionMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<AdminSubscriptionResponse>> create(
            @Valid @RequestBody AdminSubscriptionRequest request
    ) {
        Subscription created = adminSubscriptionService.createSubscription(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Subscription created successfully",
                        adminSubscriptionMapper.toResponse(created)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdminSubscriptionResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<AdminSubscriptionResponse> response = adminSubscriptionService
                .getAllSubscriptions(search, pageable)
                .map(adminSubscriptionMapper::toResponse);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Subscriptions retrieved successfully", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminSubscriptionResponse>> getById(@PathVariable Long id) {
        Subscription subscription = adminSubscriptionService.getSubscriptionById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Subscription retrieved successfully",
                        adminSubscriptionMapper.toResponse(subscription))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminSubscriptionResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody AdminSubscriptionRequest request
    ) {
        Subscription updated = adminSubscriptionService.updateSubscription(id, request);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Subscription updated successfully",
                        adminSubscriptionMapper.toResponse(updated))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        adminSubscriptionService.deleteSubscription(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Subscription deleted successfully", null)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<AdminSubscriptionResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        Subscription updated = adminSubscriptionService.updateStatus(id, status);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Subscription status updated",
                        adminSubscriptionMapper.toResponse(updated))
        );
    }

    @PatchMapping("/{id}/renew")
    public ResponseEntity<ApiResponse<AdminSubscriptionResponse>> renew(
            @PathVariable Long id,
            @RequestParam LocalDateTime newEndDate
    ) {
        Subscription renewed = adminSubscriptionService.renewSubscription(id, newEndDate);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Subscription renewed successfully",
                        adminSubscriptionMapper.toResponse(renewed))
        );
    }
}

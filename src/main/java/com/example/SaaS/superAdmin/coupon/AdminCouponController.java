package com.example.SaaS.superAdmin.coupon;


import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.common.entity.Coupon;
import com.example.SaaS.superAdmin.coupon.dto.AdminCouponRequest;
import com.example.SaaS.superAdmin.coupon.dto.AdminCouponResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/coupons")
public class AdminCouponController {
    private final AdminCouponService adminCouponService;
    private final AdminCouponMapper adminCouponMapper;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<ApiResponse<AdminCouponResponseDTO>> create(
            @Valid @ModelAttribute AdminCouponRequest request
    ) {
        Coupon saved = adminCouponService.createCoupon(request);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupon created successfully",
                        adminCouponMapper.toResponse(saved)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdminCouponResponseDTO>>> getAllCoupons(
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

        Page<AdminCouponResponseDTO> response = adminCouponService
                .getAllCoupons(search, pageable)
                .map(adminCouponMapper::toResponse);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Coupons retrieved successfully", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminCouponResponseDTO>> getCouponById(
            @PathVariable Long id
    ) {
        Coupon coupon = adminCouponService.getCouponById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupon retrieved successfully",
                        adminCouponMapper.toResponse(coupon)
                )
        );
    }

    @PutMapping(
            value = "/{id}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    public ResponseEntity<ApiResponse<AdminCouponResponseDTO>> updateCoupon(
            @PathVariable Long id,
            @Valid @ModelAttribute AdminCouponRequest request
    ) {
        Coupon updated = adminCouponService.updateCoupon(id, request);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupon updated successfully",
                        adminCouponMapper.toResponse(updated)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCouponById(@PathVariable Long id) {
        adminCouponService.deleteCouponById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Coupon deleted successfully", null)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> changeStatus(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {
        adminCouponService.changeStatus(id, active);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Coupon status updated", null)
        );
    }
}



package com.example.SaaS.company.Coupon;

import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.common.entity.Coupon;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/company/coupon")
public class CouponController {
    private final CouponService couponService;
    private final CouponMapper couponMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
            public ResponseEntity<ApiResponse< CouponResourceDTO>> create(
            @Valid @ModelAttribute CouponRequest request
    ){


        Coupon saved = couponService.createCoupon(request);
        return ResponseEntity.ok(
                new ApiResponse<CouponResourceDTO>(
                        true,
                        "coupon created successfully",
                        couponMapper.toResponse(saved)
                )
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<Page<CouponResourceDTO>>> getAllCoupons(
            @RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Page<CouponResourceDTO> response =
                couponService.getAllCoupons(pageable)
                        .map(coupon -> couponMapper.toResponse(coupon));

        return ResponseEntity.ok(
                new ApiResponse<>(true, "coupon retrieved successfully", response)    );
    }

    /* ==========================================================
   GET PACKAGE BY ID
   ========================================================== */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CouponResourceDTO>> getCouponById(
            @PathVariable Long id
    ) {
       Coupon coupon = couponService.getCouponById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "coupon retrieved successfully",
                        couponMapper.toResponse(coupon)
                )
        );
    }

    /* ==========================================================
     UPDATE PACKAGE
     ========================================================== */
    @PutMapping(
            value = "/{id}" ,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CouponResourceDTO>> updateCoupon(
            @PathVariable Long id,
            @Valid @ModelAttribute CouponRequest request
    ) {

//        Long adminId = SecurityUtil.getCurrentAdminId();




        Coupon updated = couponService.updateCoupon(id ,request);


        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "coupon updated successfully",
                        couponMapper.toResponse(updated)
                )
        );
    }


    /* ==========================================================
       DELETE PACKAGE
       ========================================================== */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCouponById(@PathVariable Long id) {

//        Long adminId = SecurityUtil.getCurrentAdminId();

        Coupon coupon = couponService.getCouponById(id);

//        // 🔒 Optional ownership check
//        if (!pkg.getAddedBy().equals(adminId)) {
//            throw new BusinessException("You are not allowed to delete this package");
//        }

        couponService.deleteCouponById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "coupon deleted successfully", null)
        );
    }
}

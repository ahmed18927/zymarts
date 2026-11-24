package com.example.SaaS.website.Coupon;

import com.example.SaaS.common.dto.ApiResponse;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;

@RestController
@RequestMapping("/api")
public class WebsiteCouponController {

    @Autowired
    private WebsiteCouponService couponService;

//    @GetMapping("/coupons")
//    public ResponseEntity<ApiResponse<Page<CouponDto>>> getValidCoupons(
//
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//
//        Pageable pageable = PageRequest.of(page, size);
//
//        Page<CouponDto> response = couponService.getValidCoupons(pageable);
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(
//                        true,
//                        "coupons retrieved successfully",
//                        response
//                )
//        );
//    }

    @GetMapping("/coupons")
    public ResponseEntity<ApiResponse<Page<CouponDto>>> getValidCoupons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<CouponDto> response = couponService.getValidCoupons(pageable);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "coupons retrieved successfully", response)
        );
    }
}

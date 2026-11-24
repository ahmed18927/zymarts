package com.example.SaaS.website.Coupon;

import com.example.SaaS.company.Coupon.CouponRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class WebsiteCouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private WebsiteCouponMapper couponMapper;

    /**
     * Get all VALID coupons with pagination
     */
    public Page<CouponDto> getValidCoupons(Pageable pageable)  {

        return couponRepository.findValidCoupons(pageable)
                .map(couponMapper::toDto);
    }
}

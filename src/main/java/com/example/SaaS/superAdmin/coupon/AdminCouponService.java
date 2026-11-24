package com.example.SaaS.superAdmin.coupon;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Coupon;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.Coupon.CouponRepository;
import com.example.SaaS.superAdmin.coupon.dto.AdminCouponRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCouponService {
    private final CouponRepository couponRepository;
    private final FileStorageService fileStorageService;
    private final CompanyRepository companyRepository;

    public Page<Coupon> getAllCoupons(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return couponRepository.findAll(pageable);
        }
        return couponRepository.findAll(pageable);
    }

    public Coupon getCouponById(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException("Coupon not found"));
    }

    public Coupon createCoupon(AdminCouponRequest request) {
        Company company = null;

        if (request.getCompanyId() != null) {
            company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new BusinessException("Company not found with ID: " + request.getCompanyId()));
        }

        Coupon coupon = Coupon.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .code(request.getCode())
                .company(company)
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .maxUsed(request.getMaxUsed())
                .isFreeShipping(request.getIsFreeShipping())
                .maxUsePerUser(request.getMaxUsePerUser())
                .appliedOnDiscount(request.getAppliedOnDiscount())
                .isActive(true)
                .build();

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imagePath = fileStorageService.saveFile(request.getImage());
            coupon.setImage(imagePath);
        }

        return couponRepository.save(coupon);
    }

    public Coupon updateCoupon(Long couponId, AdminCouponRequest request) {
        Coupon dbCoupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException("Coupon not found"));

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new BusinessException("Company not found with ID: " + request.getCompanyId()));
            dbCoupon.setCompany(company);
        }

        if (request.getCode() != null)
            dbCoupon.setCode(request.getCode());
        if (request.getDiscountType() != null)
            dbCoupon.setDiscountType(request.getDiscountType());
        if (request.getDiscountValue() != null)
            dbCoupon.setDiscountValue(request.getDiscountValue());
        if (request.getAppliedOnDiscount() != null)
            dbCoupon.setAppliedOnDiscount(request.getAppliedOnDiscount());
        if (request.getEndDate() != null)
            dbCoupon.setEndDate(request.getEndDate());
        if (request.getStartDate() != null)
            dbCoupon.setStartDate(request.getStartDate());
        if (request.getIsActive() != null)
            dbCoupon.setIsActive(request.getIsActive());
        if (request.getMaxUsed() != null)
            dbCoupon.setMaxUsed(request.getMaxUsed());
        if (request.getMaxUsePerUser() != null)
            dbCoupon.setMaxUsePerUser(request.getMaxUsePerUser());
        if (request.getIsFreeShipping() != null)
            dbCoupon.setIsFreeShipping(request.getIsFreeShipping());

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = fileStorageService.saveFile(request.getImage());
            dbCoupon.setImage(imageUrl);
        }

        return couponRepository.save(dbCoupon);
    }

    public void deleteCouponById(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException("Coupon not found"));
        couponRepository.delete(coupon);
    }

    public void changeStatus(Long couponId, boolean active) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException("Coupon not found"));
        coupon.setIsActive(active);
        couponRepository.save(coupon);
    }
}

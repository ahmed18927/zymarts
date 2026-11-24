package com.example.SaaS.company.Coupon;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Coupon;
import com.example.SaaS.common.entity.SubCategory;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.security.EmployeeDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final FileStorageService fileStorageService;
    private final CompanyRepository companyRepository;
    private Long getCurrentCompanyId() {
        return ((EmployeeDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getCompanyId();
    }

    public Page<Coupon> getAllCoupons(Pageable pageable)
    {
        Long companyId = getCurrentCompanyId();
        return couponRepository.findByCompanyId(companyId ,pageable);
//         return couponRepository.findAll();
     }

   public Coupon getCouponById(Long couponId) {
         return couponRepository.findById(couponId).
                 orElseThrow(()-> new BusinessException("COUPON NOT FOUND"));
   }
    public Coupon deleteCouponById(Long couponId) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BusinessException("COUPON NOT FOUND"));

        couponRepository.delete(coupon);

        return coupon;
    }
   public Coupon createCoupon(CouponRequest request) {
       Long companyId = getCurrentCompanyId();
       Company company = companyRepository.getReferenceById(companyId);

       Coupon coupon = Coupon.builder()
               .startDate(request.getStartDate())
               .endDate(request.getEndDate())
               .code(request.getCode())
               .company(company) // assign Company entity

               .discountType(request.getDiscountType())
               .discountValue(request.getDiscountValue())
               .maxUsed(request.getMaxUsed())
               .isFreeShipping(request.getIsFreeShipping())
               .maxUsePerUser(request.getMaxUsePerUser())
               .appliedOnDiscount(request.getAppliedOnDiscount())
               .isActive(true)
               .build();
       String imagePath = fileStorageService.saveFile( request.getImage());
       coupon.setImage(imagePath);
         return couponRepository.save(coupon);
   }

   public  Coupon updateCoupon(Long couponId, CouponRequest request) {
         Coupon dbCoupon = couponRepository.findById(couponId).orElseThrow(()->
                 new BusinessException("coupon not found"));



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

       if (request.getImage() != null && !request.getImage().isEmpty()) {
           // optional: delete old image
           // fileStorageService.deleteFile(banner.getImageUrl());
           String imageUrl = fileStorageService.saveFile(request.getImage());
           dbCoupon.setImage(imageUrl);
       }

       return couponRepository.save(dbCoupon);



   }
}

package com.example.SaaS.company;

import com.example.SaaS.common.entity.Coupon;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.company.Coupon.CouponRepository;
import com.example.SaaS.company.Coupon.CouponRequest;
import com.example.SaaS.company.Coupon.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private CouponService couponService;

    private Coupon coupon;
    private CouponRequest request;

    @BeforeEach
    void setUp() {
        coupon = Coupon.builder()
                .code("SAVE20")
                .discountType("PERCENTAGE")
                .discountValue(new BigDecimal("20.00"))
                .isActive(true)
                .maxUsed(100)
                .build();
        // نضيف ID بعد البناء
        coupon = spy(coupon);

        MockMultipartFile mockImage = new MockMultipartFile(
                "image", "coupon.jpg", "image/jpeg", "data".getBytes()
        );

        request = new CouponRequest();
        request.setCode("SAVE20");
        request.setDiscountType("PERCENTAGE");
        request.setDiscountValue(new BigDecimal("20.00"));
        request.setMaxUsed(100);
        request.setImage(mockImage);
    }
//
//    @Test
//    void getAllCoupons_shouldReturnList() {
//        when(couponRepository.findAll()).thenReturn(List.of(coupon));
//
//        List<Coupon> result = couponService.getAllCoupons();
//
//        assertThat(result).hasSize(1);
//        verify(couponRepository).findAll();
//    }

    @Test
    void getCouponById_shouldReturnCoupon_whenExists() {
        when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));

        Coupon result = couponService.getCouponById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("SAVE20");
    }

    @Test
    void getCouponById_shouldThrow_whenNotFound() {
        when(couponRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponService.getCouponById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("COUPON NOT FOUND");
    }

    @Test
    void createCoupon_shouldSaveAndReturnCoupon() {
        when(fileStorageService.saveFile(any())).thenReturn("coupon.jpg");
        when(couponRepository.save(any())).thenReturn(coupon);

        Coupon result = couponService.createCoupon(request);

        assertThat(result).isNotNull();
        verify(fileStorageService).saveFile(any());
        verify(couponRepository).save(any());
    }

    @Test
    void deleteCouponById_shouldDelete_whenFound() {
        when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));
        doNothing().when(couponRepository).delete(coupon);

        Coupon result = couponService.deleteCouponById(1L);

        assertThat(result.getCode()).isEqualTo("SAVE20");
        verify(couponRepository).delete(coupon);
    }

    @Test
    void deleteCouponById_shouldThrow_whenNotFound() {
        when(couponRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> couponService.deleteCouponById(99L))
                .isInstanceOf(BusinessException.class);

        verify(couponRepository, never()).delete(any());
    }

    @Test
    void updateCoupon_shouldUpdateFields_whenExists() {
        when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));
        when(couponRepository.save(any())).thenReturn(coupon);
        when(fileStorageService.saveFile(any())).thenReturn("new.jpg");

        request.setCode("NEWCODE");
        Coupon result = couponService.updateCoupon(1L, request);

        assertThat(result).isNotNull();
        verify(couponRepository).save(any());
    }
}
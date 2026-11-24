package com.example.SaaS.company.banner;

import com.example.SaaS.common.entity.Banner;
import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.company.category.CategoryRepository;
import com.example.SaaS.common.exception.BusinessException;
import  org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BannerServiceTest {

    // ===== الـ Mocks (النسخ الوهمية) =====
    @Mock
    private BannerRepository bannerRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private FileStorageService fileStorageService;

    // ===== الـ Service الحقيقي اللي هنختبره =====
    @InjectMocks
    private BannerService bannerService;

    // ===== بيانات مشتركة بنستخدمها في كل test =====
    private Banner banner;
    private Category category;
    private BannerRequest request;

    @BeforeEach
    void setUp() {
        // بنعمل كائنات جاهزة قبل كل test
        category = new Category();
        category.setId(1L);

        banner = new Banner();
        banner.setId(1L);
        banner.setOrdering(1);
        banner.setIsActive(true);
        banner.setLink("https://example.com");
        banner.setCategoryId(category);
        banner.setImage("banner.jpg");

        // MockMultipartFile = بديل وهمي للـ MultipartFile
        MockMultipartFile mockImage = new MockMultipartFile(
                "image", "banner.jpg", "image/jpeg", "fake-image".getBytes()
        );

        request = new BannerRequest();
        request.setOrdering(1);
        request.setIsActive(true);
        request.setLink("https://example.com");
        request.setCategoryId(1L);
        request.setImage(mockImage);
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(30));
    }

    // ============================================================
    // findAll()
    // ============================================================

    @Test
    void findAll_shouldReturnAllBanners() {
        // ARRANGE: هنقول لو bannerRepository.findAll() اتنادى → رجع list فيها banner
        when(bannerRepository.findAll()).thenReturn(List.of(banner));

        // ACT: بنشغل الميثود الحقيقية
        List<Banner> result = bannerService.findAll();

        // ASSERT: بنتأكد من النتيجة
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);

        // بنتأكد إن findAll اتنادت مرة واحدة بالظبط
        verify(bannerRepository, times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoBanners() {
        when(bannerRepository.findAll()).thenReturn(List.of());

        List<Banner> result = bannerService.findAll();

        assertThat(result).isEmpty();
    }

    // ============================================================
    // getBannerById()
    // ============================================================

    @Test
    void getBannerById_shouldReturnBanner_whenFound() {
        when(bannerRepository.findById(1L)).thenReturn(Optional.of(banner));

        Banner result = bannerService.getBannerById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getBannerById_shouldThrowException_whenNotFound() {
        when(bannerRepository.findById(99L)).thenReturn(Optional.empty());

        // بنتأكد إن BusinessException اترمت
        assertThatThrownBy(() -> bannerService.getBannerById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("banner not found");
    }

    // ============================================================
    // deleteBanner()
    // ============================================================

    @Test
    void deleteBanner_shouldDeleteSuccessfully_whenFound() {
        when(bannerRepository.findById(1L)).thenReturn(Optional.of(banner));
        // doNothing لأن delete() بترجع void
        doNothing().when(bannerRepository).delete(banner);

        Banner result = bannerService.deleteBanner(1L);

        assertThat(result.getId()).isEqualTo(1L);
        // بنتأكد إن delete اتنادت فعلاً
        verify(bannerRepository).delete(banner);
    }

    @Test
    void deleteBanner_shouldThrowException_whenNotFound() {
        when(bannerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bannerService.deleteBanner(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("banner not found");

        // بنتأكد إن delete ما اتنادتش خالص
        verify(bannerRepository, never()).delete(any());
    }

    // ============================================================
    // createBanner()
    // ============================================================

    @Test
    void createBanner_shouldCreateSuccessfully() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(fileStorageService.saveFile(any())).thenReturn("saved-banner.jpg");
        when(bannerRepository.save(any(Banner.class))).thenReturn(banner);

        Banner result = bannerService.createBanner(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        // بنتأكد إن saveFile اتنادت
        verify(fileStorageService).saveFile(any());
        verify(bannerRepository).save(any(Banner.class));
    }

    @Test
    void createBanner_shouldThrowException_whenCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bannerService.createBanner(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Category not found");

        // بنتأكد إن save ما اتنادتش
        verify(bannerRepository, never()).save(any());
    }

    // ============================================================
    // updateBanner()
    // ============================================================

    @Test
    void updateBanner_shouldUpdateSuccessfully() {
        when(bannerRepository.findById(1L)).thenReturn(Optional.of(banner));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(fileStorageService.saveFile(any())).thenReturn("updated-banner.jpg");
        when(bannerRepository.save(any(Banner.class))).thenReturn(banner);

        Banner result = bannerService.updateBanner(1L, request);

        assertThat(result).isNotNull();
        verify(bannerRepository).save(any(Banner.class));
    }

    @Test
    void updateBanner_shouldThrowException_whenBannerNotFound() {
        when(bannerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bannerService.updateBanner(99L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("banner not found");
    }

    @Test
    void updateBanner_shouldNotUpdateImage_whenImageIsNull() {
        request.setImage(null); // مفيش صورة جديدة

        when(bannerRepository.findById(1L)).thenReturn(Optional.of(banner));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category)); // ✅ زود السطر ده
        when(bannerRepository.save(any())).thenReturn(banner);

        bannerService.updateBanner(1L, request);

        verify(fileStorageService, never()).saveFile(any());
    }
}
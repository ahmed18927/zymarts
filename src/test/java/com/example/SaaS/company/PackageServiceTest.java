package com.example.SaaS.company;

import com.example.SaaS.common.entity.Package;
import com.example.SaaS.common.entity.PackageTranslation;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.superAdmin.packagee.PackageRepository;
import com.example.SaaS.superAdmin.packagee.PackageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PackageServiceTest {

    @Mock
    private PackageRepository packageRepository;

    @InjectMocks
    private PackageService packageService;

    private Package pkg;
    private PackageTranslation enTranslation;
    private PackageTranslation arTranslation;

    @BeforeEach
    void setUp() {
        enTranslation = PackageTranslation.builder()
                .locale("en").name("Basic").description("Basic plan").build();

        arTranslation = PackageTranslation.builder()
                .locale("ar").name("أساسي").description("الخطة الأساسية").build();

        pkg = Package.builder()
                .price(new BigDecimal("99.99"))
                .duration(30)
                .maxOrder(100)
                .maxProducts(50)
                .isActive(true)
                .translations(new ArrayList<>(List.of(enTranslation, arTranslation)))
                .build();
    }

    @Test
    void getAll_shouldReturnAllPackages() {
        when(packageRepository.findAll()).thenReturn(List.of(pkg));

        List<Package> result = packageService.getAll();

        assertThat(result).hasSize(1);
        verify(packageRepository).findAll();
    }

    @Test
    void getById_shouldReturnPackage_whenFound() {
        when(packageRepository.findById(1L)).thenReturn(Optional.of(pkg));

        Package result = packageService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getMaxOrder()).isEqualTo(100);
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(packageRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> packageService.getById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("package not found");
    }

    @Test
    void create_shouldSaveAndReturn() {
        when(packageRepository.save(any())).thenReturn(pkg);

        Package result = packageService.create(pkg);

        assertThat(result).isNotNull();
        verify(packageRepository).save(pkg);
    }

    @Test
    void delete_shouldDelete_whenFound() {
        when(packageRepository.findById(1L)).thenReturn(Optional.of(pkg));
        doNothing().when(packageRepository).delete(pkg);

        packageService.delete(1L);

        verify(packageRepository).delete(pkg);
    }

    // ===== اختبار resolveTranslationByLocale =====

    @Test
    void resolveTranslation_shouldReturnEnglish_whenLocaleIsEn() {
        PackageTranslation result =
                packageService.resolveTranslationByLocale(pkg.getTranslations(), "en");

        assertThat(result.getLocale()).isEqualTo("en");
        assertThat(result.getName()).isEqualTo("Basic");
    }

    @Test
    void resolveTranslation_shouldReturnArabic_whenLocaleIsAr() {
        PackageTranslation result =
                packageService.resolveTranslationByLocale(pkg.getTranslations(), "ar");

        assertThat(result.getLocale()).isEqualTo("ar");
        assertThat(result.getName()).isEqualTo("أساسي");
    }

    @Test
    void resolveTranslation_shouldFallbackToEn_whenLocaleNotFound() {
        PackageTranslation result =
                packageService.resolveTranslationByLocale(pkg.getTranslations(), "fr");

        // مفيش فرنساوي → يرجع الإنجليزي
        assertThat(result.getLocale()).isEqualTo("en");
    }

    @Test
    void resolveTranslation_shouldReturnNull_whenListIsEmpty() {
        PackageTranslation result =
                packageService.resolveTranslationByLocale(List.of(), "en");

        assertThat(result).isNull();
    }

    // ===== اختبار upsertTranslation =====

    @Test
    void upsertTranslation_shouldUpdateExisting_whenLocaleExists() {
        packageService.upsertTranslation(pkg, "en", "Premium", "Premium plan");

        PackageTranslation en = pkg.getTranslations().stream()
                .filter(t -> t.getLocale().equals("en"))
                .findFirst().orElseThrow();

        assertThat(en.getName()).isEqualTo("Premium");
    }

    @Test
    void upsertTranslation_shouldAddNew_whenLocaleNotExists() {
        packageService.upsertTranslation(pkg, "fr", "Basique", "Plan basique");

        assertThat(pkg.getTranslations()).hasSize(3);

        PackageTranslation fr = pkg.getTranslations().stream()
                .filter(t -> t.getLocale().equals("fr"))
                .findFirst().orElseThrow();

        assertThat(fr.getName()).isEqualTo("Basique");
    }
}
package com.example.SaaS.company;

import com.example.SaaS.common.entity.Country;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.superAdmin.country.CountryRepository;
import com.example.SaaS.superAdmin.country.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private CountryService countryService;

    private Country country;

    @BeforeEach
    void setUp() {
        Map<String, String> name = new HashMap<>();
        name.put("en", "Egypt");
        name.put("ar", "مصر");

        country = Country.builder()
                .code("EG")
                .name(name)
                .flag("egypt-flag.jpg")
                .build();
    }

    @Test
    void create_shouldSave_whenCodeIsUnique() {
        when(countryRepository.existsByCode("EG")).thenReturn(false);
        when(countryRepository.save(any())).thenReturn(country);

        Country result = countryService.create(country, null);

        assertThat(result.getCode()).isEqualTo("EG");
        verify(countryRepository).save(any());
    }

    @Test
    void create_shouldThrow_whenCodeExists() {
        when(countryRepository.existsByCode("EG")).thenReturn(true);

        assertThatThrownBy(() -> countryService.create(country, null))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Country already exists");

        verify(countryRepository, never()).save(any());
    }

    @Test
    void create_shouldSaveFlag_whenFlagProvided() {
        MockMultipartFile flag = new MockMultipartFile(
                "flag", "flag.png", "image/png", "data".getBytes()
        );

        when(countryRepository.existsByCode("EG")).thenReturn(false);
        when(fileStorageService.saveFile(flag)).thenReturn("saved-flag.png");
        when(countryRepository.save(any())).thenReturn(country);

        countryService.create(country, flag);

        verify(fileStorageService).saveFile(flag);
    }

    @Test
    void getById_shouldReturn_whenFound() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        Country result = countryService.getById(1L);

        assertThat(result.getCode()).isEqualTo("EG");
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(countryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> countryService.getById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Country not found");
    }

    @Test
    void delete_shouldDeleteFlagAndCountry() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        doNothing().when(fileStorageService).deleteFile("egypt-flag.jpg");
        doNothing().when(countryRepository).delete(country);

        countryService.delete(1L);

        verify(fileStorageService).deleteFile("egypt-flag.jpg");
        verify(countryRepository).delete(country);
    }

    // ===== resolveNameByLang Tests =====

    @Test
    void resolveNameByLang_shouldReturnEnglish() {
        Map<String, String> name = Map.of("en", "Egypt", "ar", "مصر");
        String result = countryService.resolveNameByLang(name, "en");
        assertThat(result).isEqualTo("Egypt");
    }

    @Test
    void resolveNameByLang_shouldReturnArabic() {
        Map<String, String> name = Map.of("en", "Egypt", "ar", "مصر");
        String result = countryService.resolveNameByLang(name, "ar");
        assertThat(result).isEqualTo("مصر");
    }

    @Test
    void resolveNameByLang_shouldFallbackToEn_whenLangNotFound() {
        Map<String, String> name = Map.of("en", "Egypt", "ar", "مصر");
        String result = countryService.resolveNameByLang(name, "fr");
        assertThat(result).isEqualTo("Egypt");
    }

    @Test
    void resolveNameByLang_shouldReturnNull_whenMapIsNull() {
        String result = countryService.resolveNameByLang(null, "en");
        assertThat(result).isNull();
    }
}
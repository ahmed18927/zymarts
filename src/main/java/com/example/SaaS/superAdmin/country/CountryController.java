package com.example.SaaS.superAdmin.country;

import com.example.SaaS.common.entity.Country;
import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.common.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;
    private final FileStorageService fileStorageService;
    private final CountryMapper countryMapper;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CountryResponseDTO>> create(
      @Valid @ModelAttribute CountryRequest request,
            @RequestParam(value = "flag", required = false) MultipartFile flag) {


        Country country = Country.builder()
                .name(Map.of(
                        "ar", request.getNameAr(),
                        "en", request.getNameEn()
                ))
                .code(request.getCode())
                .build();

        Country created = countryService.create(country, flag);
        CountryResponseDTO response =
                countryMapper.toResponse(created,
                        countryService.resolveNameByLang(created.getName(), "en"));


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Country created successfully", response));
    }



    /* ============ GET ALL ============ */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CountryResponseDTO>>> getAll(
            @RequestParam(defaultValue = "en") String lang
            ,          @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CountryResponseDTO> response =
                countryService.getAll(pageable)
                        .map(country ->
                                countryMapper.toResponse(
                                        country,
                                        countryService.resolveNameByLang(
                                                country.getName(),
                                                lang
                                        )
                                )
                        );


        return ResponseEntity.ok(
                new ApiResponse<>(true, "Countries retrieved successfully", response)
        );
    }

    /* ============ GET BY ID ============ */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CountryResponseDTO>> getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String lang) {

        Country country = countryService.getById(id);

        CountryResponseDTO response =
                countryMapper.toResponse(
                        country,
                        countryService.resolveNameByLang(country.getName(), lang)
                );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Country retrieved successfully", response)
        );
    }

    /* ============ FLAG ============ */
    @GetMapping("/{id}/flag")
    public ResponseEntity<byte[]> getFlag(@PathVariable Long id) {

        Country country = countryService.getById(id);

        if (country.getFlag() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] image = fileStorageService.loadFile(country.getFlag());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

    /* ============ UPDATE ============ */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CountryResponseDTO>> updateCoupon(
            @PathVariable Long id,
            @Valid @ModelAttribute CountryRequest request,
            @RequestParam(value = "flag", required = false) MultipartFile flag) {

        Country country = Country.builder()
                .name(Map.of(
                        "ar", request.getNameAr(),
                        "en", request.getNameEn()
                ))
                .code(request.getCode())
                .build();

        Country updated = countryService.update(id, country, flag);

        CountryResponseDTO response =
                countryMapper.toResponse(
                        updated,
                        countryService.resolveNameByLang(updated.getName(), "en")
                );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Country updated successfully", response)
        );
    }

    /* ============ DELETE ============ */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        countryService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Country deleted successfully", null)
        );
    }
}

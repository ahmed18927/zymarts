package com.example.SaaS.superAdmin.banner;


import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.common.entity.Banner;
import com.example.SaaS.superAdmin.banner.dto.AdminBannerRequest;
import com.example.SaaS.superAdmin.banner.dto.AdminBannerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/banners")
public class AdminBannerController {
    private final AdminBannerService adminBannerService;
    private final AdminBannerMapper adminBannerMapper;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<ApiResponse<AdminBannerResponse>> create(
            @Valid @ModelAttribute AdminBannerRequest request
    ) {
        Banner saved = adminBannerService.createBanner(request);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true, "Banner created successfully", adminBannerMapper.toResponse(saved)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdminBannerResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<AdminBannerResponse> response = adminBannerService
                .getAllBanners(search, pageable)
                .map(adminBannerMapper::toResponse);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Banners retrieved successfully", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminBannerResponse>> findById(@PathVariable Long id) {
        Banner banner = adminBannerService.getBannerById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Banner retrieved successfully", adminBannerMapper.toResponse(banner))
        );
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<ApiResponse<AdminBannerResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute AdminBannerRequest request
    ) {
        Banner updated = adminBannerService.updateBanner(id, request);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Banner updated successfully", adminBannerMapper.toResponse(updated))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        adminBannerService.deleteBanner(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Banner deleted successfully", null)
        );
    }
}

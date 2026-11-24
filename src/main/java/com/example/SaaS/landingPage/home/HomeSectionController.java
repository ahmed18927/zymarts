package com.example.SaaS.landingPage.home;

import com.example.SaaS.common.entity.HomeSection;
import com.example.SaaS.common.entity.HomeSectionTranslation;
import com.example.SaaS.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/home-sections")
@RequiredArgsConstructor
public class HomeSectionController {

    private final HomeSectionService homeSectionService;
    private final HomeSectionMapper homeSectionMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<HomeSectionResponse>> create(
            @Valid @RequestBody HomeSectionRequest request) {

        HomeSection created = homeSectionService.create(request);
        HomeSectionTranslation tr = homeSectionService.resolveTranslation(created, "en");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Home section created",
                        homeSectionMapper.toResponse(created, tr, "en")));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HomeSectionResponse>>> getAll(
            @RequestParam(defaultValue = "en") String lang) {

        List<HomeSectionResponse> list = homeSectionService.getAll()
                .stream()
                .map(s -> homeSectionMapper.toResponse(
                        s, homeSectionService.resolveTranslation(s, lang), lang))
                .toList();

        return ResponseEntity.ok(new ApiResponse<>(true, "Home sections retrieved", list));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<HomeSectionResponse>> getActive(
            @RequestParam(defaultValue = "en") String lang) {

        HomeSection section = homeSectionService.getActive();
        HomeSectionTranslation tr = homeSectionService.resolveTranslation(section, lang);

        return ResponseEntity.ok(new ApiResponse<>(true, "Active home section",
                homeSectionMapper.toResponse(section, tr, lang)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HomeSectionResponse>> getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String lang) {

        HomeSection section = homeSectionService.getById(id);
        HomeSectionTranslation tr = homeSectionService.resolveTranslation(section, lang);

        return ResponseEntity.ok(new ApiResponse<>(true, "Home section retrieved",
                homeSectionMapper.toResponse(section, tr, lang)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HomeSectionResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody HomeSectionRequest request) {

        HomeSection updated = homeSectionService.update(id, request);
        HomeSectionTranslation tr = homeSectionService.resolveTranslation(updated, "en");

        return ResponseEntity.ok(new ApiResponse<>(true, "Home section updated",
                homeSectionMapper.toResponse(updated, tr, "en")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        homeSectionService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Home section deleted", null));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> toggleStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        homeSectionService.toggleStatus(id, active);
        return ResponseEntity.ok(new ApiResponse<>(true, "Status updated", null));
    }
}
package com.example.SaaS.superAdmin.category;


import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.CategoryTransaction;
import com.example.SaaS.superAdmin.category.dto.AdminCategoryRequest;
import com.example.SaaS.superAdmin.category.dto.AdminCategoryResponse;
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
@RequestMapping("api/admin/categories")
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;
    private final AdminCategoryMapper adminCategoryMapper;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<ApiResponse<AdminCategoryResponse>> create(
            @Valid @ModelAttribute AdminCategoryRequest request,
            @RequestParam(defaultValue = "en") String lang
    ) {
        Category saved = adminCategoryService.createCategory(request);
        CategoryTransaction translation = adminCategoryMapper.resolveTranslationByLocale(saved.getTranslations(), lang);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true, "Category created successfully",
                        adminCategoryMapper.toResponse(saved, translation)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdminCategoryResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "en") String lang,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<AdminCategoryResponse> response = adminCategoryService
                .getAllCategories(search, pageable)
                .map(cat -> {
                    CategoryTransaction translation = adminCategoryMapper.resolveTranslationByLocale(cat.getTranslations(), lang);
                    return adminCategoryMapper.toResponse(cat, translation);
                });

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Categories retrieved successfully", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminCategoryResponse>> getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String lang
    ) {
        Category category = adminCategoryService.getCategoryById(id);
        CategoryTransaction translation = adminCategoryMapper.resolveTranslationByLocale(category.getTranslations(), lang);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Category retrieved successfully", adminCategoryMapper.toResponse(category, translation))
        );
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<ApiResponse<AdminCategoryResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute AdminCategoryRequest request,
            @RequestParam(defaultValue = "en") String lang
    ) {
        Category updated = adminCategoryService.updateCategory(id, request);
        CategoryTransaction translation = adminCategoryMapper.resolveTranslationByLocale(updated.getTranslations(), lang);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Category updated successfully", adminCategoryMapper.toResponse(updated, translation))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        adminCategoryService.deleteCategory(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Category deleted successfully", null)
        );
    }
}

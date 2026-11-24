package com.example.SaaS.website.Category;

import com.example.SaaS.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subcategories")
public class WebSubCategoryController {

    @Autowired
    private WebSubCategoryService subCategoryService;

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<SubCategoryDto>>> getByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "en") String lang,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "subcategories retrieved successfully",
                        subCategoryService.findByCategoryId(categoryId, pageable, lang)
                )
        );
    }
}

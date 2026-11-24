package com.example.SaaS.website.Category;

import com.example.SaaS.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class WebsiteCategoryController {

    @Autowired
    private WebCategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoryDto>>> getAll(
            @RequestParam(defaultValue = "en") String lang,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "categories retrieved successfully",
                        categoryService.getAllCategories(pageable, lang)
                )
        );
    }
}
package com.example.SaaS.superAdmin.product;

import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.common.entity.Product;
import com.example.SaaS.company.product.ProductService;
import com.example.SaaS.superAdmin.product.dto.AdminProductRequest;
import com.example.SaaS.superAdmin.product.dto.AdminProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Slf4j
public class AdminProductController {

    private final AdminProductService adminProductService;
    private final AdminProductMapper adminProductMapper;
    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AdminProductResponse>> create(
            @ModelAttribute @Valid AdminProductRequest request,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        log.info("Creating admin product");

        Product created = adminProductService.createProduct(request, images);

        AdminProductResponse response = adminProductMapper.toResponse(
                created,
                productService.getTranslatedName(created, "en"),
                productService.getTranslatedDescription(created, "en"),
                productService.getTranslatedSpecifications(created, "en")
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Product created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdminProductResponse>>> getAll(
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

        Page<AdminProductResponse> response = adminProductService
                .getAllProducts(search, pageable)
                .map(product -> adminProductMapper.toResponse(
                        product,
                        productService.getTranslatedName(product, lang),
                        productService.getTranslatedDescription(product, lang),
                        productService.getTranslatedSpecifications(product, lang)
                ));

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Products retrieved successfully", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminProductResponse>> getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String lang) {

        Product product = adminProductService.getProductById(id);

        AdminProductResponse response = adminProductMapper.toResponse(
                product,
                productService.getTranslatedName(product, lang),
                productService.getTranslatedDescription(product, lang),
                productService.getTranslatedSpecifications(product, lang)
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product retrieved successfully", response)
        );
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AdminProductResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute AdminProductRequest request,
            @RequestParam(value = "images", required = false) MultipartFile[] newImages,
            @RequestParam(value = "deleteImageIds", required = false) List<Long> imageIdsToDelete) {

        Product updated = adminProductService.updateProduct(id, request, newImages, imageIdsToDelete);

        AdminProductResponse response = adminProductMapper.toResponse(
                updated,
                productService.getTranslatedName(updated, "en"),
                productService.getTranslatedDescription(updated, "en"),
                productService.getTranslatedSpecifications(updated, "en")
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product updated successfully", response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        adminProductService.deleteProduct(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product deleted successfully", null)
        );
    }
}

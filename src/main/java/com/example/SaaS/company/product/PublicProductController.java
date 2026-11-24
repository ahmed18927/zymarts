package com.example.SaaS.company.product;

import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.common.entity.Product;
import com.example.SaaS.company.product.DTOS.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/companies/{companyId}/products")
@RequiredArgsConstructor
@Slf4j
public class PublicProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    /* ============ GET ALL (Public) ============ */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllPublic(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "en") String lang) {

        log.info("📨 GET /api/public/companies/{}/products (lang: {})", companyId, lang);

        List<ProductResponse> response = productService.getAllByCompany(companyId)
                .stream()
                .map(product -> productMapper.toResponse(
                        product,
                        productService.getTranslatedName(product, lang),
                        productService.getTranslatedDescription(product, lang),
                        productService.getTranslatedSpecifications(product, lang)
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Products retrieved successfully", response)
        );
    }

    /* ============ GET BY ID (Public) ============ */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getByIdPublic(
            @PathVariable Long companyId,
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String lang) {

        log.info("📨 GET /api/public/companies/{}/products/{} (lang: {})", companyId, id, lang);

        Product product = productService.getByIdForCompany(id, companyId);

        ProductResponse response = productMapper.toResponse(
                product,
                productService.getTranslatedName(product, lang),
                productService.getTranslatedDescription(product, lang),
                productService.getTranslatedSpecifications(product, lang)
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product retrieved successfully", response)
        );
    }
}

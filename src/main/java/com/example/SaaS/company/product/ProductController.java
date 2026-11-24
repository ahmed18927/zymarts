////package com.example.SaaS.company.product;
////
////import com.example.SaaS.common.entity.Product;
////import com.example.SaaS.common.response.ApiResponse;
////import com.example.SaaS.company.product.DTOS.ProductRequest;
////import com.example.SaaS.company.product.DTOS.ProductResponse;
////import jakarta.validation.Valid;
////import lombok.RequiredArgsConstructor;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.MediaType;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////import org.springframework.web.multipart.MultipartFile;
////
////import java.util.List;
////import java.util.stream.Collectors;
////
////@RestController
////@RequestMapping("/api/admin/products")
////@RequiredArgsConstructor
////public class ProductController {
////
////    private final ProductService productService;
////    private final ProductMapper productMapper;
////
////    /* ============ CREATE ============ */
//////    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//////    public ResponseEntity<ApiResponse<ProductResponse>> create(
//////          ProductRequest request,
//////            @RequestParam(value = "images", required = false) MultipartFile[] images) {
//////
//////        Product created = productService.create(request, images);
//////
//////        ProductResponse response = productMapper.toResponse(
//////                created,
//////                productService.getTranslatedName(created, "en"),
//////                productService.getTranslatedDescription(created, "en"),
//////                productService.getTranslatedSpecifications(created, "en")
//////        );
//////
//////        return ResponseEntity.status(HttpStatus.CREATED)
//////                .body(new ApiResponse<>(true, "Product created successfully", response));
//////    }
////
////    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
////    public ResponseEntity<ApiResponse<ProductResponse>> create(
////            @ModelAttribute ProductRequest request,
////            @RequestParam(value = "images", required = false) MultipartFile[] images) {
////
////        Product created = productService.create(request, images);
////
////        ProductResponse response = productMapper.toResponse(
////                created,
////                productService.getTranslatedName(created, "en"),
////                productService.getTranslatedDescription(created, "en"),
////                productService.getTranslatedSpecifications(created, "en")
////        );
////
////        return ResponseEntity.status(HttpStatus.CREATED)
////                .body(new ApiResponse<>(true, "Product created successfully", response));
////    }
////
////    /* ============ GET ALL ============ */
////    @GetMapping
////    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAll(
////            @RequestParam(defaultValue = "en") String lang) {
////
////        List<ProductResponse> response = productService.getAll()
////                .stream()
////                .map(product -> productMapper.toResponse(
////                        product,
////                        productService.getTranslatedName(product, lang),
////                        productService.getTranslatedDescription(product, lang),
////                        productService.getTranslatedSpecifications(product, lang)
////                ))
////                .collect(Collectors.toList());
////
////        return ResponseEntity.ok(
////                new ApiResponse<>(true, "Products retrieved successfully", response)
////        );
////    }
////
////    /* ============ GET BY ID ============ */
////    @GetMapping("/{id}")
////    public ResponseEntity<ApiResponse<ProductResponse>> getById(
////            @PathVariable Long id,
////            @RequestParam(defaultValue = "en") String lang) {
////
////        Product product = productService.getById(id);
////
////        ProductResponse response = productMapper.toResponse(
////                product,
////                productService.getTranslatedName(product, lang),
////                productService.getTranslatedDescription(product, lang),
////                productService.getTranslatedSpecifications(product, lang)
////        );
////
////        return ResponseEntity.ok(
////                new ApiResponse<>(true, "Product retrieved successfully", response)
////        );
////    }
////
////    /* ============ UPDATE ============ */
////    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
////    public ResponseEntity<ApiResponse<ProductResponse>> update(
////            @PathVariable Long id,
////            @Valid @ModelAttribute ProductRequest request,
////            @RequestParam(value = "images", required = false) MultipartFile[] newImages,
////            @RequestParam(value = "deleteImageIds", required = false) List<Long> imageIdsToDelete) {
////
////        Product updated = productService.update(id, request, newImages, imageIdsToDelete);
////
////        ProductResponse response = productMapper.toResponse(
////                updated,
////                productService.getTranslatedName(updated, "en"),
////                productService.getTranslatedDescription(updated, "en"),
////                productService.getTranslatedSpecifications(updated, "en")
////        );
////
////        return ResponseEntity.ok(
////                new ApiResponse<>(true, "Product updated successfully", response)
////        );
////    }
////
////    /* ============ DELETE ============ */
////    @DeleteMapping("/{id}")
////    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
////
////        productService.delete(id);
////
////        return ResponseEntity.ok(
////                new ApiResponse<>(true, "Product deleted successfully", null)
////        );
////    }
////}
//
//package com.example.SaaS.company.product;
//
//import com.example.SaaS.common.entity.Product;
//import com.example.SaaS.common.response.ApiResponse;
//import com.example.SaaS.company.product.DTOS.ProductBasicRequest;
//import com.example.SaaS.company.product.DTOS.ProductDetailsRequest;
//import com.example.SaaS.company.product.DTOS.ProductRequest;
//import com.example.SaaS.company.product.DTOS.ProductResponse;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/admin/products")
//@RequiredArgsConstructor
//public class ProductController {
//
//    private final ProductService productService;
//    private final ProductMapper productMapper;
//
//    /* ============ CREATE - STEP 1: Basic Product Info ============ */
//    @PostMapping("/step1/basic")
//    public ResponseEntity<ApiResponse<ProductResponse>> createBasicProduct(
//            @Valid @RequestBody ProductBasicRequest request) {
//
//        Product created = productService.createBasicProduct(request);
//
//        ProductResponse response = productMapper.toResponse(
//                created,
//                productService.getTranslatedName(created, "en"),
//                productService.getTranslatedDescription(created, "en"),
//                productService.getTranslatedSpecifications(created, "en")
//        );
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new ApiResponse<>(true, "Basic product info created successfully", response));
//    }
//
//    /* ============ CREATE - STEP 2: Add Product Details ============ */
//    @PostMapping("/step2/details/{productId}")
//    public ResponseEntity<ApiResponse<ProductResponse>> addProductDetails(
//            @PathVariable Long productId,
//            @Valid @RequestBody ProductDetailsRequest request) {
//
//        Product updated = productService.addProductDetails(productId, request);
//
//        ProductResponse response = productMapper.toResponse(
//                updated,
//                productService.getTranslatedName(updated, "en"),
//                productService.getTranslatedDescription(updated, "en"),
//                productService.getTranslatedSpecifications(updated, "en")
//        );
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Product details added successfully", response)
//        );
//    }
//
//    /* ============ CREATE - STEP 3: Upload Images ============ */
//    @PostMapping(value = "/step3/images/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ApiResponse<ProductResponse>> uploadProductImages(
//            @PathVariable Long productId,
//            @RequestParam(value = "images", required = true) MultipartFile[] images) {
//
//        Product updated = productService.uploadProductImages(productId, images);
//
//        ProductResponse response = productMapper.toResponse(
//                updated,
//                productService.getTranslatedName(updated, "en"),
//                productService.getTranslatedDescription(updated, "en"),
//                productService.getTranslatedSpecifications(updated, "en")
//        );
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Product images uploaded successfully", response)
//        );
//    }
//
//    /* ============ UPDATE - STEP 1: Update Basic Info ============ */
//    @PutMapping("/step1/basic/{id}")
//    public ResponseEntity<ApiResponse<ProductResponse>> updateBasicProduct(
//            @PathVariable Long id,
//            @Valid @RequestBody ProductBasicRequest request) {
//
//        Product updated = productService.updateBasicProduct(id, request);
//
//        ProductResponse response = productMapper.toResponse(
//                updated,
//                productService.getTranslatedName(updated, "en"),
//                productService.getTranslatedDescription(updated, "en"),
//                productService.getTranslatedSpecifications(updated, "en")
//        );
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Basic product info updated successfully", response)
//        );
//    }
//
//    /* ============ UPDATE - STEP 2: Update Details ============ */
//    @PutMapping("/step2/details/{id}")
//    public ResponseEntity<ApiResponse<ProductResponse>> updateProductDetails(
//            @PathVariable Long id,
//            @Valid @RequestBody ProductDetailsRequest request) {
//
//        Product updated = productService.updateProductDetails(id, request);
//
//        ProductResponse response = productMapper.toResponse(
//                updated,
//                productService.getTranslatedName(updated, "en"),
//                productService.getTranslatedDescription(updated, "en"),
//                productService.getTranslatedSpecifications(updated, "en")
//        );
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Product details updated successfully", response)
//        );
//    }
//
//    /* ============ UPDATE - STEP 3: Manage Images ============ */
//    @PutMapping(value = "/step3/images/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ApiResponse<ProductResponse>> manageProductImages(
//            @PathVariable Long id,
//            @RequestParam(value = "newImages", required = false) MultipartFile[] newImages,
//            @RequestParam(value = "deleteImageIds", required = false) List<Long> imageIdsToDelete) {
//
//        Product updated = productService.manageProductImages(id, newImages, imageIdsToDelete);
//
//        ProductResponse response = productMapper.toResponse(
//                updated,
//                productService.getTranslatedName(updated, "en"),
//                productService.getTranslatedDescription(updated, "en"),
//                productService.getTranslatedSpecifications(updated, "en")
//        );
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Product images managed successfully", response)
//        );
//    }
//
//    /* ============ OLD CREATE (ALL IN ONE) - للـ backward compatibility ============ */
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ApiResponse<ProductResponse>> create(
//            @ModelAttribute ProductRequest request,
//            @RequestParam(value = "images", required = false) MultipartFile[] images) {
//
//        Product created = productService.create(request, images);
//
//        ProductResponse response = productMapper.toResponse(
//                created,
//                productService.getTranslatedName(created, "en"),
//                productService.getTranslatedDescription(created, "en"),
//                productService.getTranslatedSpecifications(created, "en")
//        );
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new ApiResponse<>(true, "Product created successfully", response));
//    }
//
//    /* ============ OLD UPDATE (ALL IN ONE) - للـ backward compatibility ============ */
//    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ApiResponse<ProductResponse>> update(
//            @PathVariable Long id,
//            @Valid @ModelAttribute ProductRequest request,
//            @RequestParam(value = "images", required = false) MultipartFile[] newImages,
//            @RequestParam(value = "deleteImageIds", required = false) List<Long> imageIdsToDelete) {
//
//        Product updated = productService.update(id, request, newImages, imageIdsToDelete);
//
//        ProductResponse response = productMapper.toResponse(
//                updated,
//                productService.getTranslatedName(updated, "en"),
//                productService.getTranslatedDescription(updated, "en"),
//                productService.getTranslatedSpecifications(updated, "en")
//        );
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Product updated successfully", response)
//        );
//    }
//
//    /* ============ GET ALL ============ */
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAll(
//            @RequestParam(defaultValue = "en") String lang) {
//
//        List<ProductResponse> response = productService.getAll()
//                .stream()
//                .map(product -> productMapper.toResponse(
//                        product,
//                        productService.getTranslatedName(product, lang),
//                        productService.getTranslatedDescription(product, lang),
//                        productService.getTranslatedSpecifications(product, lang)
//                ))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Products retrieved successfully", response)
//        );
//    }
//
//    /* ============ GET BY ID ============ */
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<ProductResponse>> getById(
//            @PathVariable Long id,
//            @RequestParam(defaultValue = "en") String lang) {
//
//        Product product = productService.getById(id);
//
//        ProductResponse response = productMapper.toResponse(
//                product,
//                productService.getTranslatedName(product, lang),
//                productService.getTranslatedDescription(product, lang),
//                productService.getTranslatedSpecifications(product, lang)
//        );
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Product retrieved successfully", response)
//        );
//    }
//
//    /* ============ DELETE ============ */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
//
//        productService.delete(id);
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Product deleted successfully", null)
//        );
//    }
//}
package com.example.SaaS.company.product;

import com.example.SaaS.common.entity.Product;
import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.company.product.DTOS.ProductBasicRequest;
import com.example.SaaS.company.product.DTOS.ProductDetailsRequest;
import com.example.SaaS.company.product.DTOS.ProductRequest;
import com.example.SaaS.company.product.DTOS.ProductResponse;
import com.example.SaaS.security.EmployeeDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/company/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    /* ============ CREATE - STEP 1: Basic Product Info ============ */
    @PostMapping("/step1/basic")
    public ResponseEntity<ApiResponse<ProductResponse>> createBasicProduct(
            @Valid @RequestBody ProductBasicRequest request,
            @AuthenticationPrincipal EmployeeDetails employeeDetails) {

        log.info("📨 POST /api/admin/products/step1/basic - Creating basic product");

        Long companyId = employeeDetails.getCompanyId();

        Product created = productService.createBasicProduct(request, companyId);

        ProductResponse response = productMapper.toResponse(
                created,
                productService.getTranslatedName(created, "en"),
                productService.getTranslatedDescription(created, "en"),
                productService.getTranslatedSpecifications(created, "en")
        );

        log.info("✅ Basic product created with ID: {}", response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Basic product info created successfully", response));
    }

    /* ============ CREATE - STEP 2: Add Product Details ============ */
    @PostMapping("/step2/details/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> addProductDetails(
            @PathVariable Long productId,
            @Valid @RequestBody ProductDetailsRequest request,
            @AuthenticationPrincipal EmployeeDetails employeeDetails  ) {

        log.info("📨 POST /api/admin/products/step2/details/{} - Adding details", productId);

        Product updated = productService.addProductDetails(productId, request);

        ProductResponse response = productMapper.toResponse(
                updated,
                productService.getTranslatedName(updated, "en"),
                productService.getTranslatedDescription(updated, "en"),
                productService.getTranslatedSpecifications(updated, "en")
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product details added successfully", response)
        );
    }

    /* ============ CREATE - STEP 3: Upload Images ============ */
    @PostMapping(value = "/step3/images/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> uploadProductImages(
            @PathVariable Long productId,
            @RequestParam(value = "images", required = true) MultipartFile[] images,
            @AuthenticationPrincipal EmployeeDetails employeeDetails) {

        log.info("📨 POST /api/admin/products/step3/images/{} - Uploading {} images",
                productId, images.length);

        Product updated = productService.uploadProductImages(productId, images);

        ProductResponse response = productMapper.toResponse(
                updated,
                productService.getTranslatedName(updated, "en"),
                productService.getTranslatedDescription(updated, "en"),
                productService.getTranslatedSpecifications(updated, "en")
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product images uploaded successfully", response)
        );
    }

    /* ============ UPDATE - STEP 1: Update Basic Info ============ */
    @PutMapping("/step1/basic/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateBasicProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductBasicRequest request) {

        log.info("📨 PUT /api/admin/products/step1/basic/{} - Updating basic info", id);

        Product updated = productService.updateBasicProduct(id, request);

        ProductResponse response = productMapper.toResponse(
                updated,
                productService.getTranslatedName(updated, "en"),
                productService.getTranslatedDescription(updated, "en"),
                productService.getTranslatedSpecifications(updated, "en")
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Basic product info updated successfully", response)
        );
    }

    /* ============ UPDATE - STEP 2: Update Details ============ */
    @PutMapping("/step2/details/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductDetails(
            @PathVariable Long id,
            @Valid @RequestBody ProductDetailsRequest request) {

        log.info("📨 PUT /api/admin/products/step2/details/{} - Updating details", id);

        Product updated = productService.updateProductDetails(id, request);

        ProductResponse response = productMapper.toResponse(
                updated,
                productService.getTranslatedName(updated, "en"),
                productService.getTranslatedDescription(updated, "en"),
                productService.getTranslatedSpecifications(updated, "en")
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product details updated successfully", response)
        );
    }

    /* ============ UPDATE - STEP 3: Manage Images ============ */
    @PutMapping(value = "/step3/images/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> manageProductImages(
            @PathVariable Long id,
            @RequestParam(value = "newImages", required = false) MultipartFile[] newImages,
            @RequestParam(value = "deleteImageIds", required = false) List<Long> imageIdsToDelete) {

        log.info("📨 PUT /api/admin/products/step3/images/{} - Managing images", id);

        Product updated = productService.manageProductImages(id, newImages, imageIdsToDelete);

        ProductResponse response = productMapper.toResponse(
                updated,
                productService.getTranslatedName(updated, "en"),
                productService.getTranslatedDescription(updated, "en"),
                productService.getTranslatedSpecifications(updated, "en")
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product images managed successfully", response)
        );
    }

    /* ============ OLD CREATE (ALL IN ONE) ============ */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> create(
            @ModelAttribute @Valid ProductRequest request,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @AuthenticationPrincipal EmployeeDetails employeeDetails) {

        log.info("📨 POST /api/admin/products - Creating complete product");

        Long companyId = employeeDetails.getCompanyId();

        Product created = productService.create(request, images,companyId);

        ProductResponse response = productMapper.toResponse(
                created,
                productService.getTranslatedName(created, "en"),
                productService.getTranslatedDescription(created, "en"),
                productService.getTranslatedSpecifications(created, "en")
        );

        log.info("✅ Complete product created with ID: {}", response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Product created successfully", response));
    }

    /* ============ OLD UPDATE (ALL IN ONE) ============ */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductRequest request,
            @RequestParam(value = "images", required = false) MultipartFile[] newImages,
            @RequestParam(value = "deleteImageIds", required = false) List<Long> imageIdsToDelete) {

        log.info("📨 PUT /api/admin/products/{} - Updating complete product", id);

        Product updated = productService.update(id, request, newImages, imageIdsToDelete);

        ProductResponse response = productMapper.toResponse(
                updated,
                productService.getTranslatedName(updated, "en"),
                productService.getTranslatedDescription(updated, "en"),
                productService.getTranslatedSpecifications(updated, "en")
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product updated successfully", response)
        );
    }

    /* ============ GET ALL ============ */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAll(
            @RequestParam(defaultValue = "en") String lang) {

        log.info("📨 GET /api/admin/products - Getting all products (lang: {})", lang);

        List<ProductResponse> response = productService.getAll()
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

    /* ============ GET BY ID ============ */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String lang) {

        log.info("📨 GET /api/admin/products/{} (lang: {})", id, lang);

        Product product = productService.getById(id);

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

    /* ============ DELETE ============ */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        log.info("📨 DELETE /api/admin/products/{} - Deleting product", id);

        productService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product deleted successfully", null)
        );
    }
}
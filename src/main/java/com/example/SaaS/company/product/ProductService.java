////package com.example.SaaS.company.product;
////
////import com.example.SaaS.common.entity.Product;
////import com.example.SaaS.common.entity.ProductDetails;
////import com.example.SaaS.common.entity.ProductImage;
////import com.example.SaaS.common.entity.ProductTranslation;
////import com.example.SaaS.common.exception.BusinessException;
////import com.example.SaaS.common.service.FileStorageService;
////import com.example.SaaS.company.product.DTOS.ProductRequest;
////import lombok.RequiredArgsConstructor;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////import org.springframework.web.multipart.MultipartFile;
////
////import java.time.Instant;
////import java.util.ArrayList;
////import java.util.Collections;
////import java.util.List;
////
////@Service
////@RequiredArgsConstructor
////public class ProductService {
////
////    private final ProductRepository productRepository;
////    private final ProductTranslationRepository translationRepository;
////    private final FileStorageService fileStorageService;
////
////    @Transactional
////    public Product create(ProductRequest request, MultipartFile[] images) {
////
////        Product product = Product.builder()
////                .sellPrice(request.getSellPrice())
////                .stock(request.getStock())
////                .isActive(request.getIsActive())
////                .ordering(request.getOrdering())
////                .buyingPrice(request.getBuyingPrice())
////                .createdAt(Instant.now())
////                .updatedAt(Instant.now())
////                .build();
////
////        ProductDetails details = ProductDetails.builder()
////                .productId(product)
////                .sku(request.getSku())
////                .mpn(request.getMpn())
////                .discountType(request.getDiscountType())
////                .discountValue(request.getDiscountValue())
////                .startDate(request.getStartDate())
////                .endDate(request.getEndDate())
////                .views(0L)
////                .soldQuentity(0L)
////                .createdAt(Instant.now())
////                .updatedAt(Instant.now())
////                .build();
////
////        product.setProductDetails(details);
////        product = productRepository.save(product);
////
////        ProductTranslation translationAr = ProductTranslation.builder()
////                .product(product)
////                .langCode("ar")
////                .name(request.getNameAr())
////                .description(request.getDescAr())
////                .specifications(request.getSpecificationsAr())
////                .build();
////
////        ProductTranslation translationEn = ProductTranslation.builder()
////                .product(product)
////                .langCode("en")
////                .name(request.getNameEn())
////                .description(request.getDescEn())
////                .specifications(request.getSpecificationsEn())
////                .build();
////
////        translationRepository.save(translationAr);
////        translationRepository.save(translationEn);
////
////        if (images != null /* && images.length > 0 */) {
////            List<ProductImage> productImages = new ArrayList<>();
////            boolean isFirst = true;
////
////            for (MultipartFile imageFile : images) {
////                if (!imageFile.isEmpty()) {
////                    String imagePath = fileStorageService.saveFile(imageFile);
////
////                    ProductImage productImage = ProductImage.builder()
////                            .product(product)
////                            .mediaUrl(imagePath)
////                            .mediaName(imageFile.getOriginalFilename())
////                            .isMain(isFirst)
////                            .build();
////
////                    productImages.add(productImage);
////                    isFirst = false;
////                }
////            }
////            product.setImages(productImages);
////        }
////
////
////        return productRepository.save(product);
////    }
////
////    @Transactional
////    public Product update(Long id, ProductRequest request, MultipartFile[] newImages, List<Long> imageIdsToDelete) {
////
////        Product product = productRepository.findById(id)
////                .orElseThrow(() -> new BusinessException("Product not found"));
////
////        product.setSellPrice(request.getSellPrice());
////        product.setStock(request.getStock());
////        product.setIsActive(request.getIsActive());
////        product.setOrdering(request.getOrdering());
////        product.setBuyingPrice(request.getBuyingPrice());
////        product.setUpdatedAt(Instant.now());
////
////        if (product.getProductDetails() != null) {
////            ProductDetails details = product.getProductDetails();
////            details.setSku(request.getSku());
////            details.setMpn(request.getMpn());
////            details.setDiscountType(request.getDiscountType());
////            details.setDiscountValue(request.getDiscountValue());
////            details.setStartDate(request.getStartDate());
////            details.setEndDate(request.getEndDate());
////            details.setUpdatedAt(Instant.now());
////        }
////
////        ProductTranslation translationAr = translationRepository
////                .findByProductAndLangCode(product, "ar")
////                .orElse(ProductTranslation.builder()
////                        .product(product)
////                        .langCode("ar")
////                        .build());
////
////        translationAr.setName(request.getNameAr());
////        translationAr.setDescription(request.getDescAr());
////        translationAr.setSpecifications(request.getSpecificationsAr());
////        translationRepository.save(translationAr);
////
////        ProductTranslation translationEn = translationRepository
////                .findByProductAndLangCode(product, "en")
////                .orElse(ProductTranslation.builder()
////                        .product(product)
////                        .langCode("en")
////                        .build());
////
////        translationEn.setName(request.getNameEn());
////        translationEn.setDescription(request.getDescEn());
////        translationEn.setSpecifications(request.getSpecificationsEn());
////        translationRepository.save(translationEn);
////
////        if (imageIdsToDelete != null && !imageIdsToDelete.isEmpty()) {
////            List<ProductImage> imagesToRemove = new ArrayList<>();
////            for (ProductImage image : product.getImages()) {
////                if (imageIdsToDelete.contains(image.getId())) {
////                    fileStorageService.deleteFile(image.getMediaUrl());
////                    imagesToRemove.add(image);
////                }
////            }
////            product.getImages().removeAll(imagesToRemove);
////        }
////
////        if (newImages != null && newImages.length > 0) {
////            for (MultipartFile imageFile : newImages) {
////                if (!imageFile.isEmpty()) {
////                    String imagePath = fileStorageService.saveFile(imageFile);
////
////                    ProductImage productImage = ProductImage.builder()
////                            .product(product)
////                            .mediaUrl(imagePath)
////                            .mediaName(imageFile.getOriginalFilename())
////                            .isMain(product.getImages().isEmpty())
////                            .build();
////
////                    product.getImages().add(productImage);
////                }
////            }
////        }
////
////        return productRepository.save(product);
////    }
////
////    public Product getById(Long id) {
////        return productRepository.findById(id)
////                .orElseThrow(() -> new BusinessException("Product not found"));
////    }
////
////    public List<Product> getAll() {
////        return productRepository.findAll();
////    }
////
////    @Transactional
////    public void delete(Long id) {
////        Product product = getById(id);
////
////        if (product.getImages() != null) {
////            for (ProductImage image : product.getImages()) {
////                fileStorageService.deleteFile(image.getMediaUrl());
////            }
////        }
////
////        productRepository.delete(product);
////    }
////
////    /* ===== helper for translations ===== */
////    public String getTranslatedName(Product product, String lang) {
////        return translationRepository.findByProductAndLangCode(product, lang)
////                .map(ProductTranslation::getName)
////                .orElse(translationRepository.findByProductAndLangCode(product, "en")
////                        .map(ProductTranslation::getName)
////                        .orElse(null));
////    }
////
////    public String getTranslatedDescription(Product product, String lang) {
////        return translationRepository.findByProductAndLangCode(product, lang)
////                .map(ProductTranslation::getDescription)
////                .orElse(translationRepository.findByProductAndLangCode(product, "en")
////                        .map(ProductTranslation::getDescription)
////                        .orElse(null));
////    }
////
////    public String getTranslatedSpecifications(Product product, String lang) {
////        return translationRepository.findByProductAndLangCode(product, lang)
////                .map(ProductTranslation::getSpecifications)
////                .orElse(translationRepository.findByProductAndLangCode(product, "en")
////                        .map(ProductTranslation::getSpecifications)
////                        .orElse(null));
////    }
////}
//package com.example.SaaS.company.product;
//
//import com.example.SaaS.common.entity.Product;
//import com.example.SaaS.common.entity.ProductDetails;
//import com.example.SaaS.common.entity.ProductImage;
//import com.example.SaaS.common.entity.ProductTranslation;
//import com.example.SaaS.common.exception.BusinessException;
//import com.example.SaaS.common.service.FileStorageService;
//import com.example.SaaS.company.product.DTOS.ProductBasicRequest;
//import com.example.SaaS.company.product.DTOS.ProductDetailsRequest;
//import com.example.SaaS.company.product.DTOS.ProductRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ProductService {
//
//    private final ProductRepository productRepository;
//    private final ProductTranslationRepository translationRepository;
//    private final FileStorageService fileStorageService;
//
//    /* ============ STEP 1: Create Basic Product Info ============ */
//    @Transactional
//    public Product createBasicProduct(ProductBasicRequest request) {
//        Product product = Product.builder()
//                .sellPrice(request.getSellPrice())
//                .stock(Math.toIntExact(request.getStock()))
//                .isActive(request.getIsActive())
//                .ordering(request.getOrdering())
//                .buyingPrice(request.getBuyingPrice())
//                .createdAt(Instant.now())
//                .updatedAt(Instant.now())
//                .build();
//
//        product = productRepository.save(product);
//
//        // Save translations
//        ProductTranslation translationAr = ProductTranslation.builder()
//                .product(product)
//                .langCode("ar")
//                .name(request.getNameAr())
//                .description(request.getDescAr())
//                .specifications(request.getSpecificationsAr())
//                .build();
//
//        ProductTranslation translationEn = ProductTranslation.builder()
//                .product(product)
//                .langCode("en")
//                .name(request.getNameEn())
//                .description(request.getDescEn())
//                .specifications(request.getSpecificationsEn())
//                .build();
//
//        translationRepository.save(translationAr);
//        translationRepository.save(translationEn);
//
//        return product;
//    }
//
//    /* ============ STEP 2: Add Product Details ============ */
//    @Transactional
//    public Product addProductDetails(Long productId, ProductDetailsRequest request) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new BusinessException("Product not found"));
//
//        ProductDetails details = ProductDetails.builder()
//                .productId(product)
//                .sku(request.getSku())
//                .mpn(request.getMpn())
//                .discountType(request.getDiscountType())
//                .discountValue(request.getDiscountValue())
//                .startDate(request.getStartDate())
//                .endDate(request.getEndDate())
//                .views(0L)
//                .soldQuentity(0L)
//                .createdAt(Instant.now())
//                .updatedAt(Instant.now())
//                .build();
//
//        product.setProductDetails(details);
//        return productRepository.save(product);
//    }
//
//    /* ============ STEP 3: Upload Product Images ============ */
//    @Transactional
//    public Product uploadProductImages(Long productId, MultipartFile[] images) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new BusinessException("Product not found"));
//
//        if (images != null && images.length > 0) {
//            List<ProductImage> productImages = product.getImages() != null
//                    ? product.getImages()
//                    : new ArrayList<>();
//
//            boolean isFirst = productImages.isEmpty();
//
//            for (MultipartFile imageFile : images) {
//                if (!imageFile.isEmpty()) {
//                    String imagePath = fileStorageService.saveFile(imageFile);
//
//                    ProductImage productImage = ProductImage.builder()
//                            .product(product)
//                            .mediaUrl(imagePath)
//                            .mediaName(imageFile.getOriginalFilename())
//                            .isMain(isFirst)
//                            .build();
//
//                    productImages.add(productImage);
//                    isFirst = false;
//                }
//            }
//            product.setImages(productImages);
//        }
//
//        return productRepository.save(product);
//    }
//
//    /* ============ UPDATE STEP 1: Update Basic Product Info ============ */
//    @Transactional
//    public Product updateBasicProduct(Long id, ProductBasicRequest request) {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new BusinessException("Product not found"));
//
//        product.setSellPrice(request.getSellPrice());
//        product.setStock(Math.toIntExact(request.getStock()));
//        product.setIsActive(request.getIsActive());
//        product.setOrdering(request.getOrdering());
//        product.setBuyingPrice(request.getBuyingPrice());
//        product.setUpdatedAt(Instant.now());
//
//        // Update translations
//        ProductTranslation translationAr = translationRepository
//                .findByProductAndLangCode(product, "ar")
//                .orElse(ProductTranslation.builder()
//                        .product(product)
//                        .langCode("ar")
//                        .build());
//
//        translationAr.setName(request.getNameAr());
//        translationAr.setDescription(request.getDescAr());
//        translationAr.setSpecifications(request.getSpecificationsAr());
//        translationRepository.save(translationAr);
//
//        ProductTranslation translationEn = translationRepository
//                .findByProductAndLangCode(product, "en")
//                .orElse(ProductTranslation.builder()
//                        .product(product)
//                        .langCode("en")
//                        .build());
//
//        translationEn.setName(request.getNameEn());
//        translationEn.setDescription(request.getDescEn());
//        translationEn.setSpecifications(request.getSpecificationsEn());
//        translationRepository.save(translationEn);
//
//        return productRepository.save(product);
//    }
//
//    /* ============ UPDATE STEP 2: Update Product Details ============ */
//    @Transactional
//    public Product updateProductDetails(Long productId, ProductDetailsRequest request) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new BusinessException("Product not found"));
//
//        if (product.getProductDetails() != null) {
//            ProductDetails details = product.getProductDetails();
//            details.setSku(request.getSku());
//            details.setMpn(request.getMpn());
//            details.setDiscountType(request.getDiscountType());
//            details.setDiscountValue(request.getDiscountValue());
//            details.setStartDate(request.getStartDate());
//            details.setEndDate(request.getEndDate());
//            details.setUpdatedAt(Instant.now());
//        } else {
//            // If details don't exist, create new ones
//            ProductDetails details = ProductDetails.builder()
//                    .productId(product)
//                    .sku(request.getSku())
//                    .mpn(request.getMpn())
//                    .discountType(request.getDiscountType())
//                    .discountValue(request.getDiscountValue())
//                    .startDate(request.getStartDate())
//                    .endDate(request.getEndDate())
//                    .views(0L)
//                    .soldQuentity(0L)
//                    .createdAt(Instant.now())
//                    .updatedAt(Instant.now())
//                    .build();
//            product.setProductDetails(details);
//        }
//
//        return productRepository.save(product);
//    }
//
//    /* ============ UPDATE STEP 3: Manage Product Images ============ */
//    @Transactional
//    public Product manageProductImages(Long productId, MultipartFile[] newImages, List<Long> imageIdsToDelete) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new BusinessException("Product not found"));
//
//        // Delete specified images
//        if (imageIdsToDelete != null && !imageIdsToDelete.isEmpty()) {
//            List<ProductImage> imagesToRemove = new ArrayList<>();
//            for (ProductImage image : product.getImages()) {
//                if (imageIdsToDelete.contains(image.getId())) {
//                    fileStorageService.deleteFile(image.getMediaUrl());
//                    imagesToRemove.add(image);
//                }
//            }
//            product.getImages().removeAll(imagesToRemove);
//        }
//
//        // Add new images
//        if (newImages != null && newImages.length > 0) {
//            for (MultipartFile imageFile : newImages) {
//                if (!imageFile.isEmpty()) {
//                    String imagePath = fileStorageService.saveFile(imageFile);
//
//                    ProductImage productImage = ProductImage.builder()
//                            .product(product)
//                            .mediaUrl(imagePath)
//                            .mediaName(imageFile.getOriginalFilename())
//                            .isMain(product.getImages().isEmpty())
//                            .build();
//
//                    product.getImages().add(productImage);
//                }
//            }
//        }
//
//        return productRepository.save(product);
//    }
//
//    /* ============ OLD METHODS (للـ backward compatibility) ============ */
//    @Transactional
//    public Product create(ProductRequest request, MultipartFile[] images) {
//        // Step 1: Create basic product
//        ProductBasicRequest basicRequest = new ProductBasicRequest();
//        basicRequest.setSellPrice(request.getSellPrice());
//        basicRequest.setStock(request.getStock());
//        basicRequest.setIsActive(request.getIsActive());
//        basicRequest.setOrdering(request.getOrdering());
//        basicRequest.setBuyingPrice(request.getBuyingPrice());
//        basicRequest.setNameAr(request.getNameAr());
//        basicRequest.setDescAr(request.getDescAr());
//        basicRequest.setSpecificationsAr(request.getSpecificationsAr());
//        basicRequest.setNameEn(request.getNameEn());
//        basicRequest.setDescEn(request.getDescEn());
//        basicRequest.setSpecificationsEn(request.getSpecificationsEn());
//
//        Product product = createBasicProduct(basicRequest);
//
//        // Step 2: Add details
//        ProductDetailsRequest detailsRequest = new ProductDetailsRequest();
//        detailsRequest.setSku(request.getSku());
//        detailsRequest.setMpn(request.getMpn());
//        detailsRequest.setDiscountType(request.getDiscountType());
//        detailsRequest.setDiscountValue(request.getDiscountValue());
//        detailsRequest.setStartDate(request.getStartDate());
//        detailsRequest.setEndDate(request.getEndDate());
//
//        product = addProductDetails(product.getId(), detailsRequest);
//
//        // Step 3: Upload images
//        if (images != null && images.length > 0) {
//            product = uploadProductImages(product.getId(), images);
//        }
//
//        return product;
//    }
//
//    @Transactional
//    public Product update(Long id, ProductRequest request, MultipartFile[] newImages, List<Long> imageIdsToDelete) {
//        // Step 1: Update basic info
//        ProductBasicRequest basicRequest = new ProductBasicRequest();
//        basicRequest.setSellPrice(request.getSellPrice());
//        basicRequest.setStock(request.getStock());
//        basicRequest.setIsActive(request.getIsActive());
//        basicRequest.setOrdering(request.getOrdering());
//        basicRequest.setBuyingPrice(request.getBuyingPrice());
//        basicRequest.setNameAr(request.getNameAr());
//        basicRequest.setDescAr(request.getDescAr());
//        basicRequest.setSpecificationsAr(request.getSpecificationsAr());
//        basicRequest.setNameEn(request.getNameEn());
//        basicRequest.setDescEn(request.getDescEn());
//        basicRequest.setSpecificationsEn(request.getSpecificationsEn());
//
//        Product product = updateBasicProduct(id, basicRequest);
//
//        // Step 2: Update details
//        ProductDetailsRequest detailsRequest = new ProductDetailsRequest();
//        detailsRequest.setSku(request.getSku());
//        detailsRequest.setMpn(request.getMpn());
//        detailsRequest.setDiscountType(request.getDiscountType());
//        detailsRequest.setDiscountValue(request.getDiscountValue());
//        detailsRequest.setStartDate(request.getStartDate());
//        detailsRequest.setEndDate(request.getEndDate());
//
//        product = updateProductDetails(id, detailsRequest);
//
//        // Step 3: Manage images
//        if ((newImages != null && newImages.length > 0) ||
//                (imageIdsToDelete != null && !imageIdsToDelete.isEmpty())) {
//            product = manageProductImages(id, newImages, imageIdsToDelete);
//        }
//
//        return product;
//    }
//
//    /* ============ HELPER METHODS ============ */
//    public Product getById(Long id) {
//        return productRepository.findById(id)
//                .orElseThrow(() -> new BusinessException("Product not found"));
//    }
//
//    public List<Product> getAll() {
//        return productRepository.findAll();
//    }
//
//    @Transactional
//    public void delete(Long id) {
//        Product product = getById(id);
//
//        if (product.getImages() != null) {
//            for (ProductImage image : product.getImages()) {
//                fileStorageService.deleteFile(image.getMediaUrl());
//            }
//        }
//
//        productRepository.delete(product);
//    }
//
//    /* ===== helper for translations ===== */
//    public String getTranslatedName(Product product, String lang) {
//        return translationRepository.findByProductAndLangCode(product, lang)
//                .map(ProductTranslation::getName)
//                .orElse(translationRepository.findByProductAndLangCode(product, "en")
//                        .map(ProductTranslation::getName)
//                        .orElse(null));
//    }
//
//    public String getTranslatedDescription(Product product, String lang) {
//        return translationRepository.findByProductAndLangCode(product, lang)
//                .map(ProductTranslation::getDescription)
//                .orElse(translationRepository.findByProductAndLangCode(product, "en")
//                        .map(ProductTranslation::getDescription)
//                        .orElse(null));
//    }
//
//    public String getTranslatedSpecifications(Product product, String lang) {
//        return translationRepository.findByProductAndLangCode(product, lang)
//                .map(ProductTranslation::getSpecifications)
//                .orElse(translationRepository.findByProductAndLangCode(product, "en")
//                        .map(ProductTranslation::getSpecifications)
//                        .orElse(null));
//    }
//}
package com.example.SaaS.company.product;

import com.example.SaaS.common.entity.*;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.product.DTOS.ProductBasicRequest;
import com.example.SaaS.company.product.DTOS.ProductDetailsRequest;
import com.example.SaaS.company.product.DTOS.ProductRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductTranslationRepository translationRepository;
    private final FileStorageService fileStorageService;
    private final Validator validator; // ⬅️ أضف ده
    private final CompanyRepository companyRepository;

    /* ============ STEP 1: Create Basic Product Info ============ */
    @Transactional
    public Product createBasicProduct(ProductBasicRequest request, Long companyId) {

        // ✅ Validate Request
        validateBasicRequest(request);

        Company company = companyRepository.findById(companyId)
                        .orElseThrow(() -> new BusinessException("company not fond "));

        log.info("📥 Creating basic product: {}", request.getNameEn());

        Product product = Product.builder()
                .sellPrice(request.getSellPrice())
                .stock(request.getStock())
                .isActive(request.getIsActive())
                .ordering(request.getOrdering())
                .buyingPrice(request.getBuyingPrice())
                .companyId(company)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        product = productRepository.save(product);

        // Save translations
        ProductTranslation translationAr = ProductTranslation.builder()
                .product(product)
                .langCode("ar")
                .name(request.getNameAr())
                .description(request.getDescAr())
                .specifications(request.getSpecificationsAr())
                .build();

        ProductTranslation translationEn = ProductTranslation.builder()
                .product(product)
                .langCode("en")
                .name(request.getNameEn())
                .description(request.getDescEn())
                .specifications(request.getSpecificationsEn())
                .build();

        translationRepository.save(translationAr);
        translationRepository.save(translationEn);

        log.info("✅ Basic product created with ID: {}", product.getId());

        return product;
    }

    /* ============ STEP 2: Add Product Details ============ */
    @Transactional
    public Product addProductDetails(Long productId, ProductDetailsRequest request) {

        // ✅ Validate Request
        validateDetailsRequest(request);

        log.info("📥 Adding details to product ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Product not found"));

        ProductDetails details = ProductDetails.builder()
                .productId(product)
                .sku(request.getSku())
                .mpn(request.getMpn())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .views(0L)
                .soldQuentity(0L)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        product.setProductDetails(details);
        Product saved = productRepository.save(product);

        log.info("✅ Product details added successfully");

        return saved;
    }

    /* ============ STEP 3: Upload Product Images ============ */
    @Transactional
    public Product uploadProductImages(Long productId, MultipartFile[] images) {

        log.info("📥 Uploading {} images for product ID: {}", images != null ? images.length : 0, productId);

        if (images == null || images.length == 0) {
            throw new BusinessException("At least one image is required");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Product not found"));

        List<ProductImage> productImages = product.getImages() != null
                ? product.getImages()
                : new ArrayList<>();

        boolean isFirst = productImages.isEmpty();

        for (MultipartFile imageFile : images) {
            if (!imageFile.isEmpty()) {
                log.info("💾 Saving image: {}", imageFile.getOriginalFilename());
                String imagePath = fileStorageService.saveFile(imageFile);

                ProductImage productImage = ProductImage.builder()
                        .product(product)
                        .mediaUrl(imagePath)
                        .mediaName(imageFile.getOriginalFilename())
                        .isMain(isFirst)
                        .build();

                productImages.add(productImage);
                isFirst = false;
            }
        }

        product.setImages(productImages);
        Product saved = productRepository.save(product);

        log.info("✅ {} images uploaded successfully", productImages.size());

        return saved;
    }

    /* ============ UPDATE STEP 1: Update Basic Product Info ============ */
    @Transactional
    public Product updateBasicProduct(Long id, ProductBasicRequest request) {

        // ✅ Validate Request
        validateBasicRequest(request);

        log.info("🔄 Updating basic product info for ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));

        product.setSellPrice(request.getSellPrice());
        product.setStock(request.getStock());
        product.setIsActive(request.getIsActive());
        product.setOrdering(request.getOrdering());
        product.setBuyingPrice(request.getBuyingPrice());
        product.setUpdatedAt(Instant.now());

        // Update translations
        ProductTranslation translationAr = translationRepository
                .findByProductAndLangCode(product, "ar")
                .orElse(ProductTranslation.builder()
                        .product(product)
                        .langCode("ar")
                        .build());

        translationAr.setName(request.getNameAr());
        translationAr.setDescription(request.getDescAr());
        translationAr.setSpecifications(request.getSpecificationsAr());
        translationRepository.save(translationAr);

        ProductTranslation translationEn = translationRepository
                .findByProductAndLangCode(product, "en")
                .orElse(ProductTranslation.builder()
                        .product(product)
                        .langCode("en")
                        .build());

        translationEn.setName(request.getNameEn());
        translationEn.setDescription(request.getDescEn());
        translationEn.setSpecifications(request.getSpecificationsEn());
        translationRepository.save(translationEn);

        Product saved = productRepository.save(product);
        log.info("✅ Basic product info updated successfully");

        return saved;
    }

    /* ============ UPDATE STEP 2: Update Product Details ============ */
    @Transactional
    public Product updateProductDetails(Long productId, ProductDetailsRequest request) {

        // ✅ Validate Request
        validateDetailsRequest(request);

        log.info("🔄 Updating product details for ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Product not found"));

        if (product.getProductDetails() != null) {
            ProductDetails details = product.getProductDetails();
            details.setSku(request.getSku());
            details.setMpn(request.getMpn());
            details.setDiscountType(request.getDiscountType());
            details.setDiscountValue(request.getDiscountValue());
            details.setStartDate(request.getStartDate());
            details.setEndDate(request.getEndDate());
            details.setUpdatedAt(Instant.now());
        } else {
            ProductDetails details = ProductDetails.builder()
                    .productId(product)
                    .sku(request.getSku())
                    .mpn(request.getMpn())
                    .discountType(request.getDiscountType())
                    .discountValue(request.getDiscountValue())
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .views(0L)
                    .soldQuentity(0L)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();
            product.setProductDetails(details);
        }

        Product saved = productRepository.save(product);
        log.info("✅ Product details updated successfully");

        return saved;
    }

    /* ============ UPDATE STEP 3: Manage Product Images ============ */
    @Transactional
    public Product manageProductImages(Long productId, MultipartFile[] newImages, List<Long> imageIdsToDelete) {

        log.info("🔄 Managing images for product ID: {}", productId);
        log.info("📥 New images: {}, Images to delete: {}",
                newImages != null ? newImages.length : 0,
                imageIdsToDelete != null ? imageIdsToDelete.size() : 0);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("Product not found"));

        // Delete specified images
        if (imageIdsToDelete != null && !imageIdsToDelete.isEmpty()) {
            List<ProductImage> imagesToRemove = new ArrayList<>();
            for (ProductImage image : product.getImages()) {
                if (imageIdsToDelete.contains(image.getId())) {
                    log.info("🗑️ Deleting image: {}", image.getMediaName());
                    fileStorageService.deleteFile(image.getMediaUrl());
                    imagesToRemove.add(image);
                }
            }
            product.getImages().removeAll(imagesToRemove);
        }

        // Add new images
        if (newImages != null && newImages.length > 0) {
            for (MultipartFile imageFile : newImages) {
                if (!imageFile.isEmpty()) {
                    log.info("💾 Adding new image: {}", imageFile.getOriginalFilename());
                    String imagePath = fileStorageService.saveFile(imageFile);

                    ProductImage productImage = ProductImage.builder()
                            .product(product)
                            .mediaUrl(imagePath)
                            .mediaName(imageFile.getOriginalFilename())
                            .isMain(product.getImages().isEmpty())
                            .build();

                    product.getImages().add(productImage);
                }
            }
        }

        Product saved = productRepository.save(product);
        log.info("✅ Product images managed successfully");

        return saved;
    }

    /* ============ OLD METHODS (للـ backward compatibility) ============ */
    @Transactional
    public Product create(ProductRequest request, MultipartFile[] images , Long companyId) {

        // ✅ Validate Request
        validateFullRequest(request);

        log.info("📥 Creating complete product: {}", request.getNameEn());

        // Step 1: Create basic product
        ProductBasicRequest basicRequest = new ProductBasicRequest();
        basicRequest.setSellPrice(request.getSellPrice());
        basicRequest.setStock(request.getStock());
        basicRequest.setIsActive(request.getIsActive());
        basicRequest.setOrdering(request.getOrdering());
        basicRequest.setBuyingPrice(request.getBuyingPrice());
        basicRequest.setNameAr(request.getNameAr());
        basicRequest.setDescAr(request.getDescAr());
        basicRequest.setSpecificationsAr(request.getSpecificationsAr());
        basicRequest.setNameEn(request.getNameEn());
        basicRequest.setDescEn(request.getDescEn());
        basicRequest.setSpecificationsEn(request.getSpecificationsEn());

        Product product = createBasicProduct(basicRequest, companyId);

        // Step 2: Add details
        if (request.getSku() != null) {
            ProductDetailsRequest detailsRequest = new ProductDetailsRequest();
            detailsRequest.setSku(request.getSku());
            detailsRequest.setMpn(request.getMpn());
            detailsRequest.setDiscountType(request.getDiscountType());
            detailsRequest.setDiscountValue(request.getDiscountValue());
            detailsRequest.setStartDate(request.getStartDate());
            detailsRequest.setEndDate(request.getEndDate());

            product = addProductDetails(product.getId(), detailsRequest);
        }

        // Step 3: Upload images
        if (images != null && images.length > 0) {
            product = uploadProductImages(product.getId(), images);
        }

        log.info("✅ Complete product created with ID: {}", product.getId());

        return product;
    }

    @Transactional
    public Product update(Long id, ProductRequest request, MultipartFile[] newImages, List<Long> imageIdsToDelete) {

        // ✅ Validate Request
        validateFullRequest(request);

        log.info("🔄 Updating complete product ID: {}", id);

        // Step 1: Update basic info
        ProductBasicRequest basicRequest = new ProductBasicRequest();
        basicRequest.setSellPrice(request.getSellPrice());
        basicRequest.setStock(request.getStock());
        basicRequest.setIsActive(request.getIsActive());
        basicRequest.setOrdering(request.getOrdering());
        basicRequest.setBuyingPrice(request.getBuyingPrice());
        basicRequest.setNameAr(request.getNameAr());
        basicRequest.setDescAr(request.getDescAr());
        basicRequest.setSpecificationsAr(request.getSpecificationsAr());
        basicRequest.setNameEn(request.getNameEn());
        basicRequest.setDescEn(request.getDescEn());
        basicRequest.setSpecificationsEn(request.getSpecificationsEn());

        Product product = updateBasicProduct(id, basicRequest);

        // Step 2: Update details
        if (request.getSku() != null) {
            ProductDetailsRequest detailsRequest = new ProductDetailsRequest();
            detailsRequest.setSku(request.getSku());
            detailsRequest.setMpn(request.getMpn());
            detailsRequest.setDiscountType(request.getDiscountType());
            detailsRequest.setDiscountValue(request.getDiscountValue());
            detailsRequest.setStartDate(request.getStartDate());
            detailsRequest.setEndDate(request.getEndDate());

            product = updateProductDetails(id, detailsRequest);
        }

        // Step 3: Manage images
        if ((newImages != null && newImages.length > 0) ||
                (imageIdsToDelete != null && !imageIdsToDelete.isEmpty())) {
            product = manageProductImages(id, newImages, imageIdsToDelete);
        }

        log.info("✅ Complete product updated successfully");

        return product;
    }

    /* ============ VALIDATION METHODS ============ */
    private void validateBasicRequest(ProductBasicRequest request) {
        Set<ConstraintViolation<ProductBasicRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));

            log.error("❌ Basic product validation failed: {}", errorMessage);
            throw new BusinessException(errorMessage);
        }
    }

    private void validateDetailsRequest(ProductDetailsRequest request) {
        Set<ConstraintViolation<ProductDetailsRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));

            log.error("❌ Product details validation failed: {}", errorMessage);
            throw new BusinessException(errorMessage);
        }
    }

    private void validateFullRequest(ProductRequest request) {
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));

            log.error("❌ Full product validation failed: {}", errorMessage);
            throw new BusinessException(errorMessage);
        }
    }

    /* ============ HELPER METHODS ============ */
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        log.info("🗑️ Deleting product ID: {}", id);

        Product product = getById(id);

        if (product.getImages() != null) {
            for (ProductImage image : product.getImages()) {
                fileStorageService.deleteFile(image.getMediaUrl());
            }
        }

        productRepository.delete(product);
        log.info("✅ Product deleted successfully");
    }

    /* ===== Translation Helpers ===== */
    public String getTranslatedName(Product product, String lang) {
        return translationRepository.findByProductAndLangCode(product, lang)
                .map(ProductTranslation::getName)
                .orElse(translationRepository.findByProductAndLangCode(product, "en")
                        .map(ProductTranslation::getName)
                        .orElse(null));
    }

    public String getTranslatedDescription(Product product, String lang) {
        return translationRepository.findByProductAndLangCode(product, lang)
                .map(ProductTranslation::getDescription)
                .orElse(translationRepository.findByProductAndLangCode(product, "en")
                        .map(ProductTranslation::getDescription)
                        .orElse(null));
    }

    public String getTranslatedSpecifications(Product product, String lang) {
        return translationRepository.findByProductAndLangCode(product, lang)
                .map(ProductTranslation::getSpecifications)
                .orElse(translationRepository.findByProductAndLangCode(product, "en")
                        .map(ProductTranslation::getSpecifications)
                        .orElse(null));
    }

    public List<Product> getAllByCompany(Long companyId) {
        return productRepository.findAllActiveByCompanyId(companyId);
    }

    public Product getByIdForCompany(Long id, Long companyId) {
        return productRepository.findActiveByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new BusinessException("Product not found"));
    }
}
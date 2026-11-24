package com.example.SaaS.superAdmin.product;

import com.example.SaaS.common.entity.Product;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.product.ProductRepository;
import com.example.SaaS.superAdmin.product.dto.AdminProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final com.example.SaaS.company.product.ProductService productService;

    public Page<Product> getAllProducts(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));
    }

    public Product createProduct(AdminProductRequest request, MultipartFile[] images) {
        com.example.SaaS.company.product.DTOS.ProductRequest productRequest =
            com.example.SaaS.company.product.DTOS.ProductRequest.builder()
                .nameAr(request.getNameAr())
                .nameEn(request.getNameEn())
                .sellPrice(request.getSellPrice())
                .stock(request.getStock())
                .categoryId(request.getCategoryId())
                .subCategoryId(request.getSubCategoryId())
                .storeId(request.getStoreId())
                .buyingPrice(request.getBuyingPrice())
                .isActive(request.getIsActive())
                .ordering(request.getOrdering())
                .descAr(request.getDescAr())
                .descEn(request.getDescEn())
                .specificationsAr(request.getSpecificationsAr())
                .specificationsEn(request.getSpecificationsEn())
                .sku(request.getSku())
                .mpn(request.getMpn())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        return productService.create(productRequest, images, request.getCompanyId());
    }

    public Product updateProduct(Long id, AdminProductRequest request, MultipartFile[] newImages, List<Long> imageIdsToDelete) {
        com.example.SaaS.company.product.DTOS.ProductRequest productRequest =
            com.example.SaaS.company.product.DTOS.ProductRequest.builder()
                .nameAr(request.getNameAr())
                .nameEn(request.getNameEn())
                .sellPrice(request.getSellPrice())
                .stock(request.getStock())
                .categoryId(request.getCategoryId())
                .subCategoryId(request.getSubCategoryId())
                .storeId(request.getStoreId())
                .buyingPrice(request.getBuyingPrice())
                .isActive(request.getIsActive())
                .ordering(request.getOrdering())
                .descAr(request.getDescAr())
                .descEn(request.getDescEn())
                .specificationsAr(request.getSpecificationsAr())
                .specificationsEn(request.getSpecificationsEn())
                .sku(request.getSku())
                .mpn(request.getMpn())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        return productService.update(id, productRequest, newImages, imageIdsToDelete);
    }

    public void deleteProduct(Long id) {
        productService.delete(id);
    }
}

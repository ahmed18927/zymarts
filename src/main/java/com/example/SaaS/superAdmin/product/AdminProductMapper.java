package com.example.SaaS.superAdmin.product;

import com.example.SaaS.common.entity.Product;
import com.example.SaaS.company.product.DTOS.ProductImageDTO;
import com.example.SaaS.company.product.DTOS.ProductDetailsRequest;
import com.example.SaaS.superAdmin.product.dto.AdminProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminProductMapper {

    public AdminProductResponse toResponse(Product product, String name, String description, String specifications) {
        if (product == null) {
            return null;
        }

        List<ProductImageDTO> imageDTOs = product.getImages().stream()
                .map(img -> ProductImageDTO.builder()
                        .id(img.getId())
//                        .imageUrl(img.getProductDetails().)
//                        .isPrimary(img.getIsPrimary())
                        .build())
                .collect(Collectors.toList());

        return AdminProductResponse.builder()
                .id(product.getId())
                .name(name)
                .sellPrice(product.getSellPrice())
                .stock(product.getStock())
                .isActive(product.getIsActive())
                .ordering(product.getOrdering())
                .avgRate(product.getAvgRate())
                .buyingPrice(product.getBuyingPrice())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .categoryId(product.getCategoryId() != null ? product.getCategoryId().getId() : null)
                .subCategoryId(product.getSubCategoryId() != null ? product.getSubCategoryId().getId() : null)
                .companyId(product.getCompanyId() != null ? product.getCompanyId().getId() : null)
                .companyName(product.getCompanyId() != null ? product.getCompanyId().getName() : null)
                .description(description)
                .specifications(specifications)
                .sku(product.getProductDetails().getSku())
                .discountType(product.getProductDetails().getDiscountType())
                .discountValue(product.getProductDetails().getDiscountValue())
//                .startDate(product.getStartDate())
//                .endDate(product.getEndDate())
                .images(imageDTOs)
                .build();
    }
}

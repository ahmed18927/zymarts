package com.example.SaaS.company.product;

import com.example.SaaS.common.entity.Product;
import com.example.SaaS.common.entity.ProductImage;
import com.example.SaaS.company.product.DTOS.ProductImageDTO;
import com.example.SaaS.company.product.DTOS.ProductRequest;
import com.example.SaaS.company.product.DTOS.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product, String name, String description, String specifications){
        if (product == null){
            return null;
        }

        List<ProductImageDTO> imageDTOs = null;
        if (product.getImages() != null){
            imageDTOs = product.getImages().stream()
                    .map(this::toImageDTO)
                    .collect(Collectors.toList());
        }
        ProductResponse.ProductResponseBuilder builder = ProductResponse.builder()
                .id(product.getId())
                .name(name)
                . description(description)
                 .specifications(specifications)
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
                .storeId(product.getStoreId() != null ? product.getStoreId().getId() : null)
                .images(imageDTOs);
        if (product.getProductDetails() != null){
            builder
//                    description(description)
//                    .specifications(specifications)
                    .sku(product.getProductDetails().getSku())
                    .mpn(product.getProductDetails().getMpn())
                    .views(product.getProductDetails().getViews())
                    .soldQuantity(product.getProductDetails().getSoldQuentity())
                    .discountType(product.getProductDetails().getDiscountType())
                    .discountValue(product.getProductDetails().getDiscountValue())
                    .startDate(product.getProductDetails().getStartDate())
                    .endDate(product.getProductDetails().getEndDate());
        }
        return builder.build();
    }
    private ProductImageDTO toImageDTO(ProductImage image) {
        if (image == null) {
            return null;
        }

        return ProductImageDTO.builder()
                .id(image.getId())
                .mediaUrl(image.getMediaUrl())
                .isMain(image.getIsMain())
                .mediaName(image.getMediaName())
                .build();
    }
}

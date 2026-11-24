package com.example.SaaS.company.favorite;

import com.example.SaaS.common.entity.Favorite;
import com.example.SaaS.common.entity.Product;
import com.example.SaaS.common.entity.ProductImage;
import com.example.SaaS.common.entity.ProductTranslation;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    public FavoriteResponse toResponse(Favorite favorite) {

        Product product = favorite.getProductId();

        String productName = product.getTranslations()
                .stream()
                .findFirst()
                .map(ProductTranslation::getName)
                .orElse(null);

        String image = product.getImages()
                .stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsMain()))
                .findFirst()
                .map(ProductImage::getMediaUrl)
                .orElse(
                        product.getImages()
                                .stream()
                                .findFirst()
                                .map(ProductImage::getMediaUrl)
                                .orElse(null)
                );

        return FavoriteResponse.builder()
                .id(favorite.getId())
                .productId(product.getId())
                .productName(productName)
                .image(image)
                .price(product.getSellPrice())
                .build();
    }
}
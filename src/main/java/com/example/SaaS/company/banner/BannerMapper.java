package com.example.SaaS.company.banner;

import com.example.SaaS.common.entity.Banner;
import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.CategoryTransaction;
import com.example.SaaS.company.category.CategoryMinResponse;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Builder
@Component
public class BannerMapper {
    public BannerResourceDTO toResponce(Banner banner , String locale ){
        if (locale == null || locale.isBlank()) {
            locale = "en";
        }
        return BannerResourceDTO.builder()
                .id(banner.getId())
                .link(banner.getLink())
                .ordering(banner.getOrdering())
                .isActive(banner.getIsActive())
                .image(banner.getImage())
                .startDate(banner.getStartDate())
                .endDate(banner.getEndDate())
                .category(mapCategory(banner.getCategoryId(), locale))
                .build();
    }
    private CategoryMinResponse mapCategory(Category category, String locale) {
        if (category == null) return null;

        String name = category.getTranslations().stream()
                .filter(t -> t.getLocale().equalsIgnoreCase(locale))
                .map(CategoryTransaction::getName)
                .findFirst()

                .orElse(
                        category.getTranslations().stream()
                                .findFirst()
                                .map(CategoryTransaction::getName)
                                .orElse(null)
                );

        return CategoryMinResponse.builder()
                .id(category.getId())
                .name(name)
                .build();
    }
}

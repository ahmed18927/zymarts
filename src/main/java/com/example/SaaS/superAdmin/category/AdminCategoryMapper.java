package com.example.SaaS.superAdmin.category;

import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.CategoryTransaction;
import com.example.SaaS.superAdmin.category.dto.AdminCategoryResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminCategoryMapper {

    public AdminCategoryResponse toResponse(Category category, CategoryTransaction translation) {
        if (category == null) {
            return null;
        }

        String name = translation != null ? translation.getName() : null;
        String description = translation != null ? translation.getDescription() : null;

        return AdminCategoryResponse.builder()
                .id(category.getId())
                .name(name)
                .description(description)
                .isActive(category.getIsActive())
                .ordering(category.getOrdering())
                .slug(category.getSlug())
                .image(category.getImage())
                .companyId(category.getCompany() != null ? category.getCompany().getId() : null)
                .companyName(category.getCompany() != null ? category.getCompany().getName() : null)
                .build();
    }

    public CategoryTransaction resolveTranslationByLocale(List<CategoryTransaction> translations, String locale) {
        if (translations == null || translations.isEmpty()) {
            return null;
        }
        if (locale == null) locale = "en";

        for (CategoryTransaction translation : translations) {
            if (translation.getLocale().equalsIgnoreCase(locale)) {
                return translation;
            }
        }

        for (CategoryTransaction translation : translations) {
            if ("en".equalsIgnoreCase(translation.getLocale())) {
                return translation;
            }
        }

        return translations.get(0);
    }
}

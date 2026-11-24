package com.example.SaaS.website.Category;

import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.CategoryTransaction;
import org.springframework.stereotype.Component;

@Component
public class WebCategoryMapper {

    public CategoryDto toDto(Category category, String lang) {

        CategoryDto dto = new CategoryDto();

        dto.setId(category.getId());
        dto.setImage(category.getImage());

        CategoryTransaction translation = category.getTranslations()
                .stream()
                .filter(t -> t.getLocale().equalsIgnoreCase(lang))
                .findFirst()
                .orElse(
                        category.getTranslations()
                                .stream()
                                .filter(t -> t.getLocale().equalsIgnoreCase("en"))
                                .findFirst()
                                .orElse(null)
                );

        if (translation != null) {
            dto.setName(translation.getName());
        }

        return dto;
    }
}

package com.example.SaaS.company.subCategory;

import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.CategoryTransaction;
import com.example.SaaS.common.entity.SubCategory;
import com.example.SaaS.common.entity.SubCategoryTranslation;
import com.example.SaaS.company.category.CategoryMinResponse;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class SubCategoryMapper {

    public SubCategoryResourceDTO toResponse(
            SubCategory sub , SubCategoryTranslation translation
    ){
        return SubCategoryResourceDTO.builder()
                .id(sub.getId())

                .name(translation !=  null ?translation.getName():null)
                .description(translation != null ?translation.getDescription():null)
                .ordering(sub.getOrdering())
                .parent(mapParent(sub.getParentId(), "en"))
                .category(mapCategory(sub.getCategoryId(), "en"))

                .isActive(sub.getIsActive())
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
    private SubCategoryMinResourceDTO mapParent(SubCategory parent, String locale) {
        if (parent == null) return null;
        return SubCategoryMinResourceDTO.builder()
                .id(parent.getId())
                .name(parent.getName())
                .build();
    }

}

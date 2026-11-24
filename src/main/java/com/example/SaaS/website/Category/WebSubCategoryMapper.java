package com.example.SaaS.website.Category;

import com.example.SaaS.common.entity.CategoryTransaction;
import com.example.SaaS.common.entity.SubCategory;
import com.example.SaaS.common.entity.SubCategoryTranslation;
import org.springframework.stereotype.Component;

@Component
public class WebSubCategoryMapper {


        public SubCategoryDto toDto(SubCategory subCategory, String lang) {

            SubCategoryDto dto = new SubCategoryDto();

            dto.setId(subCategory.getId());
//            dto.setImage(subCategory.getImage());

            // subcategory translation
            SubCategoryTranslation subTranslation =
                    subCategory.getTranslations()
                            .stream()
                            .filter(t -> t.getLocale().equalsIgnoreCase(lang))
                            .findFirst()
                            .orElse(
                                    subCategory.getTranslations()
                                            .stream()
                                            .filter(t -> t.getLocale().equalsIgnoreCase("en"))
                                            .findFirst()
                                            .orElse(null)
                            );

            if (subTranslation != null) {
                dto.setName(subTranslation.getName());
            }

            // category info
            SubCategoryDto.CategoryInfo categoryInfo =
                    new SubCategoryDto.CategoryInfo();

            categoryInfo.setId(subCategory.getCategoryId().getId());

            CategoryTransaction categoryTranslation =
                    subCategory.getCategoryId().getTranslations()
                            .stream()
                            .filter(t -> t.getLocale().equalsIgnoreCase(lang))
                            .findFirst()
                            .orElse(
                                    subCategory.getCategoryId().getTranslations()
                                            .stream()
                                            .filter(t -> t.getLocale().equalsIgnoreCase("en"))
                                            .findFirst()
                                            .orElse(null)
                            );

            if (categoryTranslation != null) {
                categoryInfo.setName(categoryTranslation.getName());
            }

            dto.setCategory(categoryInfo);

            return dto;
        }
}

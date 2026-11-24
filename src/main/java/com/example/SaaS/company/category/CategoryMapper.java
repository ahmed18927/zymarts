package com.example.SaaS.company.category;

import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.CategoryTransaction;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class CategoryMapper{

    public CategoryResourceDTO toResponse(
            Category cat ,
            CategoryTransaction translation
    ){
        return CategoryResourceDTO.builder()
                .id(cat.getId())
                .name(translation != null ? translation.getName() : null)
                .description(translation != null ?translation.getDescription():null)
                .slug(cat.getSlug())
                .ordering(cat.getOrdering())
                .image(cat.getImage())
                .isActive(cat.getIsActive())
                .build();
    }

}

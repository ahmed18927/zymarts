package com.example.SaaS.superAdmin.feature;

import com.example.SaaS.common.entity.Feature;
import com.example.SaaS.common.entity.FeatureTranslation;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class FeatureMapper {
    public FeatureResourceDTO toResponce(Feature feature , FeatureTranslation translation
    ) {
        return FeatureResourceDTO.builder()
                        .id(feature.getId())
                .name(translation != null ?translation.getName():null)
                .description(translation != null ?translation.getDescription():null)
                .ordering(feature.getOrdering())
                .isActive(feature.isActive())
                .slug(feature.getSlug())
                .startDate(feature.getStartDate())
                .endDate(feature.getEndDate())
//                .image(feature.getImage)
                .build();
    }
}

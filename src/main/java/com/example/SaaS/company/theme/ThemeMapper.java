package com.example.SaaS.company.theme;

import com.example.SaaS.common.entity.Theme;
import org.springframework.stereotype.Component;

@Component
public class ThemeMapper {
    public ThemeResourceDTO toResponse(Theme theme){
        return ThemeResourceDTO.builder()
                .id(theme.getId())
                .name(theme.getName())
                .description(theme.getDescription())
                .cssVariables(theme.getCssVariables())
                .layout(theme.getLayout())
                .isActive(theme.getIsActive())
                .build();
    }
}

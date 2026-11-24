package com.example.SaaS.company.theme;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThemeResourceDTO {
    private Long id;
    private String name;
    private String description;
    private Map<String, String> cssVariables;
    private String layout;
    private Boolean isActive;
}

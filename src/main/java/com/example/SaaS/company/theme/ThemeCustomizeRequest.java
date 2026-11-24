package com.example.SaaS.company.theme;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ThemeCustomizeRequest {
    @NotEmpty(message = "customThemeSettings must not be empty")
    private Map<String, String> customThemeSettings;
}

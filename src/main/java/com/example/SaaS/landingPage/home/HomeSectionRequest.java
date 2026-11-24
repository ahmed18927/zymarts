package com.example.SaaS.landingPage.home;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HomeSectionRequest {

    @NotBlank(message = "Hero title (EN) is required")
    private String heroTitleEn;

    @NotBlank(message = "Hero subtitle (EN) is required")
    private String heroSubtitleEn;

    @NotBlank(message = "CTA primary text (EN) is required")
    private String ctaPrimaryTextEn;

    @NotBlank(message = "CTA secondary text (EN) is required")
    private String ctaSecondaryTextEn;

    // Arabic translations
    private String heroTitleAr;
    private String heroSubtitleAr;
    private String ctaPrimaryTextAr;
    private String ctaSecondaryTextAr;

    // Links
    private String ctaPrimaryLink;
    private String ctaSecondaryLink;

    // Stats
    private Integer totalClients;
    private Integer totalOrders;
    private Integer totalProducts;

    private Boolean isActive;
}
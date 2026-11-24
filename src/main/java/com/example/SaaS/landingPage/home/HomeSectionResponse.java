package com.example.SaaS.landingPage.home;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HomeSectionResponse {
    private Long id;
    private String heroTitle;
    private String heroSubtitle;
    private String ctaPrimaryText;
    private String ctaSecondaryText;
    private String ctaPrimaryLink;
    private String ctaSecondaryLink;
    private Integer totalClients;
    private Integer totalOrders;
    private Integer totalProducts;
    private Boolean isActive;
    private String lang;
}
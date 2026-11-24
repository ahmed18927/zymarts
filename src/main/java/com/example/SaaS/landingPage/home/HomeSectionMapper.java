package com.example.SaaS.landingPage.home;

import com.example.SaaS.common.entity.HomeSection;
import com.example.SaaS.common.entity.HomeSectionTranslation;
import org.springframework.stereotype.Component;

@Component
public class HomeSectionMapper {

    public HomeSectionResponse toResponse(HomeSection section,
                                          HomeSectionTranslation tr,
                                          String lang) {
        return HomeSectionResponse.builder()
                .id(section.getId())
                .heroTitle(tr != null ? tr.getHeroTitle() : section.getHeroTitleEn())
                .heroSubtitle(tr != null ? tr.getHeroSubtitle() : section.getHeroSubtitleEn())
                .ctaPrimaryText(tr != null ? tr.getCtaPrimaryText() : section.getCtaPrimaryTextEn())
                .ctaSecondaryText(tr != null ? tr.getCtaSecondaryText() : section.getCtaSecondaryTextEn())
                .ctaPrimaryLink(section.getCtaPrimaryLink())
                .ctaSecondaryLink(section.getCtaSecondaryLink())
                .totalClients(section.getTotalClients())
                .totalOrders(section.getTotalOrders())
                .totalProducts(section.getTotalProducts())
                .isActive(section.getIsActive())
                .lang(lang)
                .build();
    }
}

package com.example.SaaS.landingPage.home;

import com.example.SaaS.common.entity.HomeSection;
import com.example.SaaS.common.entity.HomeSectionTranslation;
import com.example.SaaS.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeSectionService {

    private final HomeSectionRepository homeSectionRepository;
    private final HomeSectionTranslationRepository translationRepository;

    @Transactional
    public HomeSection create(HomeSectionRequest request){

        HomeSection section = HomeSection.builder()
                .heroTitleEn(request.getHeroTitleEn())
                .heroSubtitleEn(request.getHeroSubtitleEn())
                .ctaPrimaryTextEn(request.getCtaPrimaryTextEn())
                .ctaSecondaryTextEn(request.getCtaSecondaryTextEn())
                .ctaPrimaryLink(request.getCtaPrimaryLink())
                .ctaSecondaryLink(request.getCtaSecondaryLink())
                .totalClients(request.getTotalClients())
                .totalOrders(request.getTotalOrders())
                .totalProducts(request.getTotalProducts())
                .isActive(true)
                .build();

        section = homeSectionRepository.save(section);
        saveTranslations(section, request);
        return section;
    }

    @Transactional
    public HomeSection update(Long id, HomeSectionRequest request) {

        HomeSection section = getById(id);

        section.setHeroTitleEn(request.getHeroTitleEn());
        section.setHeroSubtitleEn(request.getHeroSubtitleEn());
        section.setCtaPrimaryTextEn(request.getCtaPrimaryTextEn());
        section.setCtaSecondaryTextEn(request.getCtaSecondaryTextEn());
        section.setCtaPrimaryLink(request.getCtaPrimaryLink());
        section.setCtaSecondaryLink(request.getCtaSecondaryLink());
        section.setTotalClients(request.getTotalClients());
        section.setTotalOrders(request.getTotalOrders());
        section.setTotalProducts(request.getTotalProducts());

        if (request.getIsActive() != null) {
            section.setIsActive(request.getIsActive());
        }

        upsertTranslation(section, "en",
                request.getHeroTitleEn(), request.getHeroSubtitleEn(),
                request.getCtaPrimaryTextEn(), request.getCtaSecondaryTextEn());

        upsertTranslation(section, "ar",
                request.getHeroTitleAr(), request.getHeroSubtitleAr(),
                request.getCtaPrimaryTextAr(), request.getCtaSecondaryTextAr());

        return homeSectionRepository.save(section);
    }

    public HomeSection getById(Long id) {
        return homeSectionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Home section not found"));
    }

    // ⚡ مهم: جيب الـ active section للـ landing page
    public HomeSection getActive() {
        return homeSectionRepository.findFirstByIsActiveTrueOrderByIdDesc()
                .orElseThrow(() -> new BusinessException("No active home section found"));
    }

    public List<HomeSection> getAll() {
        return homeSectionRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        homeSectionRepository.delete(getById(id));
    }

    @Transactional
    public void toggleStatus(Long id, boolean active) {
        HomeSection section = getById(id);
        section.setIsActive(active);
        homeSectionRepository.save(section);
    }

    // ─── helpers ────────────────────────────────────────────

    private void saveTranslations(HomeSection section, HomeSectionRequest req) {
        translationRepository.save(HomeSectionTranslation.builder()
                .homeSection(section).langCode("en")
                .heroTitle(req.getHeroTitleEn())
                .heroSubtitle(req.getHeroSubtitleEn())
                .ctaPrimaryText(req.getCtaPrimaryTextEn())
                .ctaSecondaryText(req.getCtaSecondaryTextEn())
                .build());

        if (req.getHeroTitleAr() != null) {
            translationRepository.save(HomeSectionTranslation.builder()
                    .homeSection(section).langCode("ar")
                    .heroTitle(req.getHeroTitleAr())
                    .heroSubtitle(req.getHeroSubtitleAr())
                    .ctaPrimaryText(req.getCtaPrimaryTextAr())
                    .ctaSecondaryText(req.getCtaSecondaryTextAr())
                    .build());
        }
    }

    private void upsertTranslation(HomeSection section, String lang,
                                   String title, String subtitle,
                                   String ctaPrimary, String ctaSecondary) {
        HomeSectionTranslation tr = translationRepository
                .findByHomeSectionAndLangCode(section, lang)
                .orElse(HomeSectionTranslation.builder()
                        .homeSection(section).langCode(lang).build());

        tr.setHeroTitle(title);
        tr.setHeroSubtitle(subtitle);
        tr.setCtaPrimaryText(ctaPrimary);
        tr.setCtaSecondaryText(ctaSecondary);
        translationRepository.save(tr);
    }

    // resolve الترجمة مع fallback
    public HomeSectionTranslation resolveTranslation(HomeSection section, String lang) {
        return translationRepository
                .findByHomeSectionAndLangCode(section, lang)
                .orElseGet(() -> translationRepository
                        .findByHomeSectionAndLangCode(section, "en")
                        .orElse(null));
    }
}

package com.example.SaaS.landingPage.home;

import com.example.SaaS.common.entity.HomeSection;
import com.example.SaaS.common.entity.HomeSectionTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HomeSectionTranslationRepository extends JpaRepository<HomeSectionTranslation, Long> {
    Optional<HomeSectionTranslation> findByHomeSectionAndLangCode(
            HomeSection homeSection, String langCode);
}

package com.example.SaaS.seeder;

import com.example.SaaS.common.entity.Feature;
import com.example.SaaS.common.entity.FeatureTranslation;
import com.example.SaaS.superAdmin.feature.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FeatureSeeder implements CommandLineRunner {
    private final FeatureRepository featureRepository;

    @Override
    public void run(String... args) {
        if (featureRepository.count() > 0) return; // avoid duplicates
        seedFeatures();
    }

    private void seedFeatures() {

        createFeature(
                "multi-language-support",
                "SYSTEM",
                1,
                true,
                LocalDate.now(),
                LocalDate.now().plusYears(5),

                "Multi Language Support",
                "دعم تعدد اللغات",
                "Allow system to support multiple languages",
                "يسمح للنظام بدعم لغات متعددة"
        );

        createFeature(
                "advanced-analytics",
                "BUSINESS",
                2,
                true,
                LocalDate.now(),
                LocalDate.now().plusYears(5),

                "Advanced Analytics",
                "تحليلات متقدمة",
                "Advanced business analytics dashboard",
                "لوحة تحليلات أعمال متقدمة"
        );
    }

    private void createFeature(
            String slug,
            String type,
            Integer ordering,
            Boolean isActive,
            LocalDate startDate,
            LocalDate endDate,

            String nameEn,
            String nameAr,
            String descEn,
            String descAr
    ) {
        Feature feature = Feature.builder()
                .slug(slug)
                .type(type)
                .ordering(ordering)
                .isActive(isActive)
                .startDate(startDate)
                .endDate(endDate)
                .translations(List.of(
                        FeatureTranslation.builder()
                                .locale("en")
                                .name(nameEn)
                                .description(descEn)
                                .feature(null) // will be set by cascade
                                .build(),
                        FeatureTranslation.builder()
                                .locale("ar")
                                .name(nameAr)
                                .description(descAr)
                                .feature(null)
                                .build()
                ))
                .build();

        // Set back-reference for translations
        feature.getTranslations().forEach(t -> t.setFeature(feature));

        featureRepository.save(feature); // saves translations automatically
    }
}

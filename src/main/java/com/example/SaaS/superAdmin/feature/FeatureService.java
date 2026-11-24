package com.example.SaaS.superAdmin.feature;

import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.CategoryTransaction;
import com.example.SaaS.common.entity.Feature;
import com.example.SaaS.common.entity.FeatureTranslation;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureService {
    private final FeatureRepository featureRepository;
    private final FileStorageService  fileStorageService;
    public Page<Feature> getAllFeatures(Pageable pageable) {
        return featureRepository.findAll(pageable);
    }
    public Feature getFeatureById(Long id) {
        return featureRepository.findById(id).orElseThrow(
                ()-> new BusinessException("feature not found")
        );
    }
    public Feature delete(Long id) {
        Feature feature = featureRepository.findById(id).orElseThrow(
                () -> new BusinessException("feature not found")
        );
        featureRepository.delete(feature);
        return feature;
    }
    public Feature createFeature(FeatureRequest request) {
        Feature feature = Feature.builder()
                        .isActive(true)
                .ordering(request.getOrdering())
                .slug(request.getSlug())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                    .build();
        List<FeatureTranslation> translations = List.of(
          FeatureTranslation.builder()
                  .name(request.getNameEn())
                  .description(request.getDescriptionEn())
                  .locale("en")
                  .feature(feature)
                  .build(),
                FeatureTranslation.builder()
                        .name(request.getNameAr())
                        .description(request.getDescriptionAr())
                        .locale("ar")
                        .feature(feature)
                        .build()

        );
        String imagePath = fileStorageService.saveFile(request.getImage());
                feature.setImage(imagePath);
                feature.setTranslations(translations);
                return  featureRepository.save(feature);
    }

    public Feature updateFeature( Long id ,FeatureRequest request ) {

        Feature dbFeature = featureRepository.findById(id)
                .orElseThrow(() -> new BusinessException("feature not found"));



        // -------- IMAGE (optional) --------
        if (request.getImage() != null && !request.getImage().isEmpty()) {

            // optional: delete old image
            // fileStorageService.deleteFile(banner.getImageUrl());

            String imageUrl = fileStorageService.saveFile(request.getImage());
            dbFeature.setImage(imageUrl);
        }


        // -------- OTHER FIELDS --------
        if (request.getOrdering() != null)
            dbFeature.setOrdering(request.getOrdering());



        if (request.getIsActive() != null)
            dbFeature.setActive(request.getIsActive());

        if (request.getSlug() != null)
            dbFeature.setSlug(request.getSlug());

        if (request.getStartDate() != null)
            dbFeature.setStartDate(request.getStartDate());

        if (request.getEndDate() != null)
            dbFeature.setEndDate(request.getEndDate());


        upsertTranslation(dbFeature ,"en",
                request.getNameEn() , request.getDescriptionEn());
        upsertTranslation(dbFeature ,"ar",
                request.getNameAr() , request.getDescriptionAr());

        return featureRepository.save(dbFeature);
    }
    public void upsertTranslation(
            Feature feature,
            String locale,
            String name,
            String description
    ) {
        for (FeatureTranslation translation : feature.getTranslations()) {
            if (translation.getLocale().equalsIgnoreCase(locale)) {
                // ✅ UPDATE EXISTING
                translation.setName(name);
                translation.setDescription(description);
                return;
            }
        }

        // ✅ CREATE ONLY IF NOT EXISTS
        FeatureTranslation newTranslation = FeatureTranslation.builder()
                .locale(locale)
                .name(name)
                .description(description)
                .feature(feature)
                .build();

        feature.getTranslations().add(newTranslation);
    }

    public FeatureTranslation resolveTranslationByLocale(
            List<FeatureTranslation> translations , String locale
    ){
        if(translations==null || translations.isEmpty()){
            return null;
        }
        if(locale == null) locale ="en";
        for(FeatureTranslation translation : translations){
            if(translation.getLocale().equalsIgnoreCase(locale)){
                return translation;
            }
        }
        // 2️⃣ English fallback
        for (FeatureTranslation translation : translations) {
            if ("en".equalsIgnoreCase(translation.getLocale())) {
                return translation;
            }
        }

        // 3️⃣ First available
        return translations.get(0);
    }
}

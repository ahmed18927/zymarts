package com.example.SaaS.superAdmin.packagee;

import com.example.SaaS.common.entity.Country;
import com.example.SaaS.common.entity.PackageTranslation;
import com.example.SaaS.common.entity.Package;
import com.example.SaaS.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {
    private final PackageRepository packageRepository;

    public List<Package> getAll() {
        return packageRepository.findAll();
    }
     public Package create(Package pkg){
         return packageRepository.save(pkg);
     }
     public Package getById(Long id){
         return packageRepository.findById(id)
                 .orElseThrow(() -> new BusinessException("package not found"));
     }
     public PackageTranslation resolveTranslationByLocale(
             List<PackageTranslation> translations , String locale
     ){
         if(translations==null || translations.isEmpty()){
             return null;
         }
         if(locale == null) locale ="en";
         for(PackageTranslation translation : translations){
             if(translation.getLocale().equalsIgnoreCase(locale)){
                 return translation;
             }
         }
         // 2️⃣ English fallback
         for (PackageTranslation translation : translations) {
             if ("en".equalsIgnoreCase(translation.getLocale())) {
                 return translation;
             }
         }

         // 3️⃣ First available
         return translations.get(0);
     }

    /* ================= UPDATE ================= */
    public Package update(Long id, Package existing) {

        // 🔒 Safety check (optional but good)
        Package dbPackage = packageRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Package not found"));

        // Copy updated scalar fields
        dbPackage.setPrice(existing.getPrice());
        dbPackage.setDuration(existing.getDuration());
        dbPackage.setOrdering(existing.getOrdering());
        dbPackage.setMaxOrder(existing.getMaxOrder());
        dbPackage.setMaxProducts(existing.getMaxProducts());
        dbPackage.setIsActive(existing.getIsActive());

        // ⚠️ IMPORTANT:
        // Translations are ALREADY updated in controller via upsertTranslation()
        // So we DO NOT touch translations here

        return packageRepository.save(dbPackage);
    }
    public void upsertTranslation(
            Package pkg,
            String locale,
            String name,
            String description
    ) {
        for (PackageTranslation translation : pkg.getTranslations()) {
            if (translation.getLocale().equalsIgnoreCase(locale)) {
                // ✅ UPDATE EXISTING
                translation.setName(name);
                translation.setDescription(description);
                return;
            }
        }

        // ✅ CREATE ONLY IF NOT EXISTS
        PackageTranslation newTranslation = PackageTranslation.builder()
                .locale(locale)
                .name(name)
                .description(description)
                .pkg(pkg)
                .build();

        pkg.getTranslations().add(newTranslation);
    }

    public void delete(Long id) {
        Package apackage = getById(id);
        packageRepository.delete(apackage);
    }
}

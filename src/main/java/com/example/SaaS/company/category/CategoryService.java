package com.example.SaaS.company.category;

import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.CategoryTransaction;
import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.security.EmployeeDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
private final FileStorageService fileStorageService;
    private final CompanyRepository companyRepository;

    private Long getCurrentCompanyId() {
        return ((EmployeeDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getCompanyId();
    }

    public Page<Category> getAll(Pageable  pageable) {
        Long companyId = getCurrentCompanyId();
        return categoryRepository.findByCompanyId(companyId ,pageable);
//        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("category not found"));
    }
    public Category delete(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category NOT FOUND"));

        categoryRepository.delete(category);
        return category;
    }
    public Category create(CategoryRequest request) {
        Long companyId = getCurrentCompanyId();
        Company company = companyRepository.getReferenceById(companyId);

        Category cat = Category.builder()
                .isActive(true)
                .company(company) // assign Company entity
                .ordering(request.getOrdering())
                .build();
        List<CategoryTransaction>  translations = List.of(
                CategoryTransaction.builder()
                        .locale("en")
                        .name(request.getNameEn())
                        .description(request.getDescriptionEn())
                        .category(cat).build(),
                CategoryTransaction.builder()
                        .locale("ar")
                        .name(request.getNameAr())
                        .description(request.getDescriptionAr())
                        .category(cat).build()

        );
        String imagePath = fileStorageService.saveFile( request.getImage());
        cat.setImage(imagePath);
        cat.setTranslations(translations);

        return categoryRepository.save(cat);
    }
    public CategoryTransaction resolveTranslationByLocale(
            List<CategoryTransaction> translations , String locale
    ){
        if(translations==null || translations.isEmpty()){
            return null;
        }
        if(locale == null) locale ="en";
        for(CategoryTransaction translation : translations){
            if(translation.getLocale().equalsIgnoreCase(locale)){
                return translation;
            }
        }
        // 2️⃣ English fallback
        for (CategoryTransaction translation : translations) {
            if ("en".equalsIgnoreCase(translation.getLocale())) {
                return translation;
            }
        }

        // 3️⃣ First available
        return translations.get(0);
    }

    // ================= UPDATE =================
    public Category updateCategory(Long id, CategoryRequest request) {
        // 1. Fetch existing category, or throw if not found
        Category dbCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found"));



        // -------- IMAGE (optional) --------
        if (request.getImage() != null && !request.getImage().isEmpty()) {

            // optional: delete old image
            // fileStorageService.deleteFile(banner.getImageUrl());

            String imageUrl = fileStorageService.saveFile(request.getImage());
            dbCategory.setImage(imageUrl);
        }


        // -------- OTHER FIELDS --------
        if (request.getOrdering() != null)
            dbCategory.setOrdering(request.getOrdering());



        if (request.getIsActive() != null)
            dbCategory.setIsActive(request.getIsActive());

        if (request.getSlug() != null)
            dbCategory.setSlug(request.getSlug());


       upsertTranslation(dbCategory ,"en",
                request.getNameEn() , request.getDescriptionEn());
       upsertTranslation(dbCategory ,"ar",
                request.getNameAr() , request.getDescriptionAr());

        return categoryRepository.save(dbCategory);
    }
    public void upsertTranslation(
            Category cat,
            String locale,
            String name,
            String description
    ) {
        for (CategoryTransaction translation : cat.getTranslations()) {
            if (translation.getLocale().equalsIgnoreCase(locale)) {
                // ✅ UPDATE EXISTING
                translation.setName(name);
                translation.setDescription(description);
                return;
            }
        }

        // ✅ CREATE ONLY IF NOT EXISTS
        CategoryTransaction newTranslation = CategoryTransaction.builder()
                .locale(locale)
                .name(name)
                .description(description)
                .category(cat)
                .build();

        cat.getTranslations().add(newTranslation);
    }
}

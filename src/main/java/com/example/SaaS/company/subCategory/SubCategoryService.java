package com.example.SaaS.company.subCategory;

import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.CategoryTransaction;
import com.example.SaaS.common.entity.SubCategory;
import com.example.SaaS.common.entity.SubCategoryTranslation;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    public SubCategory create(SubCategoryRequest request){

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        SubCategory parent = request.getParentId() == null
                ? null
                : subCategoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));
        SubCategory subcat = SubCategory.builder().
                isActive(true)
                .categoryId(category)
                .parentId(parent)
                .ordering(request.getOrdering()).

                build();
        List<SubCategoryTranslation> translations =List.of(
                SubCategoryTranslation.builder()
                        .locale("en")
                        .name(request.getNameEn())
                        .subCategory(subcat).build(),
                SubCategoryTranslation.builder().locale("ar").name(request.getNameAr()).build()
        );
        subcat.setTranslations(translations);

        return subCategoryRepository.save(subcat);
    }

    public SubCategory delete(Long id){

        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("sub Category NOT FOUND"));
        subCategoryRepository.delete(subCategory);
        return subCategory;
    }
    public SubCategory getById(Long id){
        return subCategoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("sub category not found"));
    }
    public Page<SubCategory> getAll(Pageable  pageable){
        return subCategoryRepository.findAll(pageable);
    }

    public SubCategoryTranslation resolveTranslationByLocale(
            List<SubCategoryTranslation> translations , String locale
    ){
        if(translations==null || translations.isEmpty()){
            return null;
        }
        if(locale == null) locale ="en";
        for(SubCategoryTranslation translation : translations){
            if(translation.getLocale().equalsIgnoreCase(locale)){
                return translation;
            }
        }
        // 2️⃣ English fallback
        for (SubCategoryTranslation translation : translations) {
            if ("en".equalsIgnoreCase(translation.getLocale())) {
                return translation;
            }
        }

        // 3️⃣ First available
        return translations.get(0);
    }

    /* ================= UPDATE ================= */
    public SubCategory update(Long id, SubCategoryRequest request) {

        // 🔒 Safety check (optional but good)
        SubCategory dbSubCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Sub Category not found"));


        if (request.getOrdering() != null)
            dbSubCategory.setOrdering(request.getOrdering());
        if (request.getIsActive() != null)
            dbSubCategory.setIsActive(request.getIsActive());


        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            dbSubCategory.setCategoryId(category);
        }
        if (request.getParentId() != null) {
            SubCategory subCat = subCategoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("subcategory not found"));

            dbSubCategory.setParentId(subCat);
        }

        upsertTranslation(dbSubCategory ,"en" ,
                request.getNameEn() , request.getDescriptionEn());
        upsertTranslation(dbSubCategory ,"ar" ,
                request.getNameAr() ,request.getDescriptionAr());

        return subCategoryRepository.save(dbSubCategory);
    }
    public void upsertTranslation(
            SubCategory subCat,
            String locale,
            String name,
            String description
    ) {
        for (SubCategoryTranslation translation : subCat.getTranslations()) {
            if (translation.getLocale().equalsIgnoreCase(locale)) {
                // ✅ UPDATE EXISTING
                translation.setName(name);
                translation.setDescription(description);
                return;
            }
        }

        // ✅ CREATE ONLY IF NOT EXISTS
        SubCategoryTranslation newTranslation = SubCategoryTranslation.builder()
                .locale(locale)
                .name(name)
                .description(description)
                .subCategory(subCat)
                .build();

        subCat.getTranslations().add(newTranslation);
    }
}

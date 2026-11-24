package com.example.SaaS.superAdmin.category;

import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.CategoryTransaction;
import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.category.CategoryRepository;
import com.example.SaaS.superAdmin.category.dto.AdminCategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;
    private final CompanyRepository companyRepository;

    public Page<Category> getAllCategories(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return categoryRepository.findAll(pageable);
        }
        return categoryRepository.findAll(pageable);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found"));
    }

    public Category createCategory(AdminCategoryRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new BusinessException("Company not found with ID: " + request.getCompanyId()));

        Category cat = Category.builder()
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .company(company)
                .ordering(request.getOrdering())
                .slug(request.getSlug())
                .build();

        List<CategoryTransaction> translations = List.of(
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

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imagePath = fileStorageService.saveFile(request.getImage());
            cat.setImage(imagePath);
        }

        cat.setTranslations(translations);

        return categoryRepository.save(cat);
    }

    public Category updateCategory(Long id, AdminCategoryRequest request) {
        Category dbCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found"));

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new BusinessException("Company not found with ID: " + request.getCompanyId()));
            dbCategory.setCompany(company);
        }

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = fileStorageService.saveFile(request.getImage());
            dbCategory.setImage(imageUrl);
        }

        if (request.getOrdering() != null)
            dbCategory.setOrdering(request.getOrdering());

        if (request.getIsActive() != null)
            dbCategory.setIsActive(request.getIsActive());

        if (request.getSlug() != null)
            dbCategory.setSlug(request.getSlug());

        upsertTranslation(dbCategory, "en", request.getNameEn(), request.getDescriptionEn());
        upsertTranslation(dbCategory, "ar", request.getNameAr(), request.getDescriptionAr());

        return categoryRepository.save(dbCategory);
    }

    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    private void upsertTranslation(Category cat, String locale, String name, String description) {
        for (CategoryTransaction translation : cat.getTranslations()) {
            if (translation.getLocale().equalsIgnoreCase(locale)) {
                translation.setName(name);
                translation.setDescription(description);
                return;
            }
        }

        CategoryTransaction newTranslation = CategoryTransaction.builder()
                .locale(locale)
                .name(name)
                .description(description)
                .category(cat)
                .build();

        cat.getTranslations().add(newTranslation);
    }
}

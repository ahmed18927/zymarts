package com.example.SaaS.superAdmin.banner;

import com.example.SaaS.common.entity.Banner;
import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.banner.BannerRepository;
import com.example.SaaS.company.category.CategoryRepository;
import com.example.SaaS.superAdmin.banner.dto.AdminBannerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminBannerService {
    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;
    private final CompanyRepository companyRepository;

    public Page<Banner> getAllBanners(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return bannerRepository.findAll(pageable);
        }
        return bannerRepository.findAll(pageable);
    }

    public Banner getBannerById(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Banner not found"));
    }

    public Banner createBanner(AdminBannerRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new BusinessException("Company not found with ID: " + request.getCompanyId()));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException("Category not found"));

        Banner banner = new Banner();
        banner.setCompanyId(company);
        banner.setOrdering(request.getOrdering());
        banner.setIsActive(request.getIsActive());
        banner.setLink(request.getLink());
        banner.setStartDate(request.getStartDate());
        banner.setEndDate(request.getEndDate());
        banner.setCategoryId(category);

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imagePath = fileStorageService.saveFile(request.getImage());
            banner.setImage(imagePath);
        }

        return bannerRepository.save(banner);
    }

    public Banner updateBanner(Long bannerId, AdminBannerRequest request) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new BusinessException("Banner not found"));

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new BusinessException("Company not found with ID: " + request.getCompanyId()));
            banner.setCompanyId(company);
        }

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = fileStorageService.saveFile(request.getImage());
            banner.setImage(imageUrl);
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException("Category not found"));
            banner.setCategoryId(category);
        }

        if (request.getLink() != null)
            banner.setLink(request.getLink());
        if (request.getOrdering() != null)
            banner.setOrdering(request.getOrdering());
        if (request.getIsActive() != null)
            banner.setIsActive(request.getIsActive());
        if (request.getStartDate() != null)
            banner.setStartDate(request.getStartDate());
        if (request.getEndDate() != null)
            banner.setEndDate(request.getEndDate());

        return bannerRepository.save(banner);
    }

    public void deleteBanner(Long id) {
        Banner banner = getBannerById(id);
        bannerRepository.delete(banner);
    }
}

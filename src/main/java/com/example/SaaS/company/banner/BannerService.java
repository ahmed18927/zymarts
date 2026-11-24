package com.example.SaaS.company.banner;

import com.example.SaaS.common.entity.Banner;
import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Employee;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.category.CategoryRepository;
import com.example.SaaS.security.EmployeeDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;
    private final CompanyRepository companyRepository;

    private Long getCurrentCompanyId() {
        return ((EmployeeDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getCompanyId();
    }

    public Page<Banner> findAll(Pageable  pageable) {
        Long companyId = getCurrentCompanyId();
        return bannerRepository.findByCompanyId(companyId , pageable );
//        return bannerRepository.findAll();
    }
    public Banner getBannerById(Long id) {
        return bannerRepository.findById(id).orElseThrow(
                () -> new BusinessException("banner not found")
        );

    }
    public  Banner deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id).orElseThrow(
                () -> new BusinessException("banner not found")
        );

        bannerRepository.delete(banner);
        return banner;
    }
    public Banner createBanner(BannerRequest request) {
        Long companyId = getCurrentCompanyId();
        Company company = companyRepository.getReferenceById(companyId);

        Banner banner = new Banner();
        banner.setCompanyId(company);

        banner.setOrdering(request.getOrdering());
        banner.setIsActive(request.getIsActive());
        banner.setLink(request.getLink());
        banner.setStartDate(request.getStartDate());
        banner.setEndDate(request.getEndDate());

        // CATEGORY
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        banner.setCategoryId(category);

        // IMAGE
//        MultipartFile image = request.getImage();
//        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.saveFile( request.getImage());
            banner.setImage(imagePath);
//        }

        return   bannerRepository.save(banner);
//        return bannerRepository.save(banner);
    }
    public Banner updateBanner(Long bannerId,BannerRequest request) {
        Banner banner = bannerRepository.findById(bannerId).orElseThrow(
                () -> new BusinessException("banner not found")
        );
        // -------- IMAGE (optional) --------
        if (request.getImage() != null && !request.getImage().isEmpty()) {

            // optional: delete old image
            // fileStorageService.deleteFile(banner.getImageUrl());

            String imageUrl = fileStorageService.saveFile(request.getImage());
            banner.setImage(imageUrl);
        }

        // -------- CATEGORY (optional) --------
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            banner.setCategoryId(category);
        }

        // -------- OTHER FIELDS --------
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

}

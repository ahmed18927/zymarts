package com.example.SaaS.superAdmin.banner;

import com.example.SaaS.common.entity.Banner;
import com.example.SaaS.company.category.CategoryMinResponse;
import com.example.SaaS.superAdmin.banner.dto.AdminBannerResponse;
import org.springframework.stereotype.Component;

@Component
public class AdminBannerMapper {

    public AdminBannerResponse toResponse(Banner banner) {
        if (banner == null) {
            return null;
        }

        CategoryMinResponse categoryResponse = null;
        if (banner.getCategoryId() != null) {
            categoryResponse = CategoryMinResponse.builder()
                    .id(banner.getCategoryId().getId())
                    .name(banner.getCategoryId().getSlug())
                    .build();
        }

        return AdminBannerResponse.builder()
                .id(banner.getId())
                .link(banner.getLink())
                .ordering(banner.getOrdering())
                .isActive(banner.getIsActive())
                .image(banner.getImage())
                .startDate(banner.getStartDate())
                .endDate(banner.getEndDate())
                .category(categoryResponse)
                .companyId(banner.getCompanyId() != null ? banner.getCompanyId().getId() : null)
                .companyName(banner.getCompanyId() != null ? banner.getCompanyId().getName() : null)
                .build();
    }
}

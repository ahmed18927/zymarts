package com.example.SaaS.superAdmin.banner.dto;

import com.example.SaaS.company.category.CategoryMinResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AdminBannerResponse {
    private Long id;
    private String link;
    private Integer ordering;
    private Boolean isActive;
    private String image;
    private LocalDate startDate;
    private LocalDate endDate;
    private CategoryMinResponse category;
    private Long companyId;
    private String companyName;
}

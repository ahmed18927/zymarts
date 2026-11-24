package com.example.SaaS.company.banner;

import com.example.SaaS.common.entity.Category;
import com.example.SaaS.company.category.CategoryMinResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BannerResourceDTO {
    private Long id;
    private String link;
    private Integer ordering;
    private Boolean isActive;
    private String image;
    private LocalDate startDate;
    private LocalDate endDate;
    private CategoryMinResponse category;
}

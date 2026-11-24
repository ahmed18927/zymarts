package com.example.SaaS.superAdmin.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
    private Integer ordering;
    private String slug;
    private String image;
    private Long companyId;
    private String companyName;
}

package com.example.SaaS.company.category;

import lombok.*;

@Data
@Builder
public class CategoryResourceDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
    private Integer ordering;
    private String slug;
    private String image;
}

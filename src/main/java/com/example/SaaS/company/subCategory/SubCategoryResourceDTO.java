package com.example.SaaS.company.subCategory;

import com.example.SaaS.company.category.CategoryMinResponse;
import lombok.*;

@Data
@Builder
public class SubCategoryResourceDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Boolean isActive;
    private Integer ordering;
    private CategoryMinResponse category;
    private SubCategoryMinResourceDTO parent;

}

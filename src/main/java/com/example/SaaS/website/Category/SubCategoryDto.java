package com.example.SaaS.website.Category;

import lombok.Data;

@Data
public class SubCategoryDto {

    private Long id;
    private String name;
    private String image;
    private CategoryInfo category;

    @Data
    public static class CategoryInfo {
        private Long id;
        private String name;
    }
}

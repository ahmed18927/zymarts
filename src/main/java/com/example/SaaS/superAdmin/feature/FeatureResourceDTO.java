package com.example.SaaS.superAdmin.feature;

import lombok.*;

import java.time.LocalDate;


@Data
@Builder
public class FeatureResourceDTO {
    private Long id;
    private String description;
    private String slug;
    private String type;
    private Boolean isActive;
    private LocalDate startDate;
    private LocalDate endDate;
    private String image;
    private String  name;
    private Integer ordering;
}

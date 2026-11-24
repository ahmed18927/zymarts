package com.example.SaaS.superAdmin.packagee;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
public class PackageResponseDTO {
    private  long id;
    private String name;
    private String description;
    private Integer ordering;
    private BigDecimal price;
    private Boolean isActive;
    private Integer duration;
}

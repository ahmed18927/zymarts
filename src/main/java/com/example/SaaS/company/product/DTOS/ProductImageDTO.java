package com.example.SaaS.company.product.DTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {
    private Long id;
    private String mediaUrl;
    private Boolean isMain;
    private String mediaName;
}

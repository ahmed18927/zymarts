package com.example.SaaS.superAdmin.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.SaaS.company.product.DTOS.ProductImageDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminProductResponse {
    private Long id;
    private String name;
    private BigDecimal sellPrice;
    private Integer stock;
    private Boolean isActive;
    private Integer ordering;
    private BigDecimal avgRate;
    private BigDecimal buyingPrice;
    private Instant createdAt;
    private Instant updatedAt;
    private Long categoryId;
    private Long subCategoryId;
    private Long companyId;
    private String companyName;
    private String description;
    private String specifications;
    private String sku;
    private String mpn;
    private Long views;
    private Long soldQuantity;
    private String discountType;
    private BigDecimal discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<ProductImageDTO> images;
}

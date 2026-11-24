package com.example.SaaS.company.product.DTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

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

    // Related entities IDs
    private Long categoryId;
    private Long subCategoryId;
    private Long storeId;

    // Product Details
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

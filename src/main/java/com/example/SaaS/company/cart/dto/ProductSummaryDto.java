package com.example.SaaS.company.cart.dto;

import java.math.BigDecimal;

public record ProductSummaryDto(
        Long productId,
        String mainImageUrl,
        BigDecimal sellPrice,
        Boolean isActive,
        Integer stock
) {}

package com.example.SaaS.company.cart.dto;

import java.math.BigDecimal;

public record CartItemResponseDto(
        Long itemId,
        ProductSummaryDto product,
        Integer quantity,
        BigDecimal price,
        BigDecimal subtotal,
        Long couponId
) {}
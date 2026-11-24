package com.example.SaaS.company.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequestDto(

        @NotNull(message = "productVariantId is required")
        Long productVariantId,

        @NotNull(message = "quantity is required")
        @Min(value = 1, message = "quantity must be at least 1")
        Integer quantity,

        // Optional. Stored as-is. NOT used to compute any discount here.
        Long couponId
) {}

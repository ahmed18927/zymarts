package com.example.SaaS.company.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponseDto(
        Long cartId,
        String guestToken,
        List<CartItemResponseDto> items,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal total
) {}
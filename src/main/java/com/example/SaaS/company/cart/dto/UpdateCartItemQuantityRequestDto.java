package com.example.SaaS.company.cart.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateCartItemQuantityRequestDto(
        @NotNull(message = "action is required")
        CartItemAction action
) {
    public enum CartItemAction {
        INCREMENT,
        DECREMENT
    }
}
package com.example.SaaS.company.cart.service;

import com.example.SaaS.common.entity.Customer;
import com.example.SaaS.company.cart.dto.AddCartItemRequestDto;
import com.example.SaaS.company.cart.dto.CartResponseDto;
import com.example.SaaS.company.cart.dto.UpdateCartItemQuantityRequestDto;

public interface CartService {

    CartResponseDto getCart(String customerEmail, String guestToken);

    CartResponseDto addItem(String customerEmail, String guestToken, AddCartItemRequestDto request);

    CartResponseDto updateItemQuantity(String customerEmail, String guestToken, Long itemId,
                                       UpdateCartItemQuantityRequestDto request);

    CartResponseDto deleteItem(String customerEmail, String guestToken, Long itemId);

    void clearCart(String customerEmail, String guestToken);

    /** Called from the auth flow right after a guest logs in or registers. */
    void mergeGuestCartIntoCustomer(String guestToken, Customer customer);

    CartResponseDto applyCoupon(String customerEmail, String guestToken, String couponCode);

    CartResponseDto removeCoupon(String customerEmail, String guestToken);
}
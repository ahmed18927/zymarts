package com.example.SaaS.company.cart.exception;

public class CartItemNotFoundException extends RuntimeException {
  public CartItemNotFoundException(Long itemId) {
    super("Cart item not found with id: " + itemId);
  }
}
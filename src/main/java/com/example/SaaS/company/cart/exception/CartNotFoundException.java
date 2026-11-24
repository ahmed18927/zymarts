package com.example.SaaS.company.cart.exception;

public class CartNotFoundException extends RuntimeException {
  public CartNotFoundException(String identifier) {
    super("Cart not found: " + identifier);
  }
}
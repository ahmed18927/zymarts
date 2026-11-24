package com.example.SaaS.company.cart.exception;

public class ProductNotActiveException extends RuntimeException {
    public ProductNotActiveException(Long productId) {
        super("Product is not active, id: " + productId);
    }
}

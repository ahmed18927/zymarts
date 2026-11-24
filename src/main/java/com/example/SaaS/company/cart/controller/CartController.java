package com.example.SaaS.company.cart.controller;

import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.company.cart.dto.AddCartItemRequestDto;
import com.example.SaaS.company.cart.dto.CartResponseDto;
import com.example.SaaS.company.cart.dto.UpdateCartItemQuantityRequestDto;
import com.example.SaaS.company.cart.service.CartService;
import com.example.SaaS.company.cart.util.CartContextResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Cart operations for authenticated users and guests")
public class CartController {

    private final CartService cartService;
    private final CartContextResolver contextResolver;

    @GetMapping
    @Operation(summary = "Get the current cart (authenticated user or guest)")
    public ResponseEntity<ApiResponse<CartResponseDto>> getCart(HttpServletRequest request) {
        String email = contextResolver.resolveAuthenticatedEmail();
        String guestToken = contextResolver.resolveGuestToken(request);
        CartResponseDto cart = cartService.getCart(email, guestToken);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cart retrieved", cart));
    }

    @PostMapping("/items")
    @Operation(summary = "Add an item to the cart, or increase quantity if it already exists")
    public ResponseEntity<ApiResponse<CartResponseDto>> addItem(
            HttpServletRequest request,
            @Valid @RequestBody AddCartItemRequestDto body) {
        String email = contextResolver.resolveAuthenticatedEmail();
        String guestToken = contextResolver.resolveGuestToken(request);
        CartResponseDto cart = cartService.addItem(email, guestToken, body);
        return ResponseEntity.ok(new ApiResponse<>(true, "Item added", cart));
    }

    @PatchMapping("/items/{itemId}")
    @Operation(summary = "Increment or decrement a cart item's quantity")
    public ResponseEntity<ApiResponse<CartResponseDto>> updateItemQuantity(
            HttpServletRequest request,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemQuantityRequestDto body) {
        String email = contextResolver.resolveAuthenticatedEmail();
        String guestToken = contextResolver.resolveGuestToken(request);
        CartResponseDto cart = cartService.updateItemQuantity(email, guestToken, itemId, body);
        return ResponseEntity.ok(new ApiResponse<>(true, "Item updated", cart));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remove a single item from the cart")
    public ResponseEntity<ApiResponse<CartResponseDto>> deleteItem(
            HttpServletRequest request,
            @PathVariable Long itemId) {
        String email = contextResolver.resolveAuthenticatedEmail();
        String guestToken = contextResolver.resolveGuestToken(request);
        CartResponseDto cart = cartService.deleteItem(email, guestToken, itemId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Item removed", cart));
    }

    @DeleteMapping
    @Operation(summary = "Clear the entire cart")
    public ResponseEntity<ApiResponse<Void>> clearCart(HttpServletRequest request) {
        String email = contextResolver.resolveAuthenticatedEmail();
        String guestToken = contextResolver.resolveGuestToken(request);
        cartService.clearCart(email, guestToken);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cart cleared", null));
    }

    @PostMapping("/coupon")
    @Operation(summary = "Apply a coupon code to the cart")
    public ResponseEntity<ApiResponse<CartResponseDto>> applyCoupon(
            HttpServletRequest request,
            @RequestParam String code) {
        String email = contextResolver.resolveAuthenticatedEmail();
        String guestToken = contextResolver.resolveGuestToken(request);
        CartResponseDto cart = cartService.applyCoupon(email, guestToken, code);
        return ResponseEntity.ok(new ApiResponse<>(true, "Coupon applied", cart));
    }

    @DeleteMapping("/coupon")
    @Operation(summary = "Remove the coupon from the cart")
    public ResponseEntity<ApiResponse<CartResponseDto>> removeCoupon(HttpServletRequest request) {
        String email = contextResolver.resolveAuthenticatedEmail();
        String guestToken = contextResolver.resolveGuestToken(request);
        CartResponseDto cart = cartService.removeCoupon(email, guestToken);
        return ResponseEntity.ok(new ApiResponse<>(true, "Coupon removed", cart));
    }
}
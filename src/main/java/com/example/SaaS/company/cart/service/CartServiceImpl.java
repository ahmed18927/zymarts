package com.example.SaaS.company.cart.service;

import com.example.SaaS.common.entity.*;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.Coupon.CouponRepository;
import com.example.SaaS.company.cart.dto.AddCartItemRequestDto;
import com.example.SaaS.company.cart.dto.CartItemResponseDto;
import com.example.SaaS.company.cart.dto.CartResponseDto;
import com.example.SaaS.company.cart.dto.UpdateCartItemQuantityRequestDto;
import com.example.SaaS.company.cart.exception.*;
import com.example.SaaS.company.cart.mapper.CartMapper;
import com.example.SaaS.company.cart.repository.CartItemRepository;
import com.example.SaaS.company.cart.repository.CartRepository;
import com.example.SaaS.company.customer.CustomerRepository;
import com.example.SaaS.company.product.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CartMapper cartMapper;
    private final CouponRepository couponRepository;


    // -------------------- GET /cart --------------------

//    @Override
//    @Transactional(readOnly = true)
//    public CartResponseDto getCart(String customerEmail, String guestToken) {
//        Cart cart = findExistingCart(customerEmail, guestToken)
//                .orElseGet(() -> Cart.builder()
//                        .items(List.of())
//                        .build());
//
//        if (cart.getId() == null) {
//            // No persisted cart yet — return an empty shell without hitting the DB again.
//            List<CartItemResponseDto> emptyItems = List.of();
//            return new CartResponseDto(null, emptyItems, BigDecimal.ZERO, null, BigDecimal.ZERO);
//        }
//
//        List<CartItemResponseDto> itemDtos = cartMapper.toItemDtoList(cart.getItems());
//        return cartMapper.toCartDto(cart, itemDtos, null);
//    }

    @Override
    @Transactional(readOnly = true)
    public CartResponseDto getCart(String customerEmail, String guestToken) {

        Cart cart = findExistingCart(customerEmail, guestToken).orElse(null);

        if (cart == null || cart.getId() == null) {
            return new CartResponseDto(
                    null,
                    guestToken,
                    List.of(),
                    BigDecimal.ZERO,
                    null,
                    BigDecimal.ZERO
            );
        }

        System.out.println("customerEmail = " + customerEmail);
        System.out.println("guestToken = " + guestToken);

        List<CartItem> freshItems = cartItemRepository.findAllByCartId(cart.getId());
        List<CartItemResponseDto> itemDtos = cartMapper.toItemDtoList(freshItems);

        return cartMapper.toCartDto(cart, itemDtos, cart.getCouponId());
    }

    // -------------------- POST /cart/items --------------------

    @Override
    @Transactional
    public CartResponseDto addItem(String customerEmail, String guestToken, AddCartItemRequestDto request) {

        Product product = productRepository.findById(request.productVariantId())
                .orElseThrow(() -> new ProductNotFoundException(request.productVariantId()));

        validateProductPurchasable(product, request.quantity());

        Cart cart = findOrCreateCart(customerEmail, guestToken);

        // لو مفيش Cart اعمل واحدة جديدة
        if (cart == null) {

            Customer customer = null;

            if (customerEmail != null) {
                customer = customerRepository.findByEmail(customerEmail)
                        .orElseThrow(() -> new CartNotFoundException(customerEmail));
            }

            cart = createEmptyCart(
                    customer,
                    customerEmail == null ? guestToken : null,
                    product.getStoreId(),
                    product.getCompanyId()
            );
        }

        CartItem existingItem = cartItemRepository
                .findByCartId_IdAndProductId_Id(cart.getId(), product.getId())
                .orElse(null);

        if (existingItem != null) {

            int newQuantity = existingItem.getQuantity() + request.quantity();

            ensureStock(product, newQuantity);

            existingItem.setQuantity(newQuantity);
            existingItem.setPrice(product.getSellPrice());

            if (request.couponId() != null) {
                existingItem.setCoupon(Coupon.builder().id(request.couponId()).build());
            }

            cartItemRepository.save(existingItem);

        } else {

            CartItem newItem = CartItem.builder()
                    .cartId(cart)
                    .productId(product)
                    .quantity(request.quantity())
                    .price(product.getSellPrice())
                    .coupon(request.couponId() != null
                            ? Coupon.builder().id(request.couponId()).build()
                            : null)
                    .build();

            cartItemRepository.save(newItem);
        }

        return getCart(customerEmail, guestToken);
    }

    // -------------------- PATCH /cart/items/{itemId} --------------------

    @Override
    @Transactional
    public CartResponseDto updateItemQuantity(String customerEmail, String guestToken, Long itemId,
                                              UpdateCartItemQuantityRequestDto request) {
        Cart cart = findExistingCart(customerEmail, guestToken)
                .orElseThrow(() -> new CartNotFoundException(cartIdentifier(customerEmail, guestToken)));

        CartItem item = cartItemRepository.findByIdAndCartId_Id(itemId, cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException(itemId));

        int delta = request.action() == UpdateCartItemQuantityRequestDto.CartItemAction.INCREMENT ? 1 : -1;
        int newQuantity = item.getQuantity() + delta;

        if (newQuantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            Product product = item.getProductId();
            ensureStock(product, newQuantity);
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        }

        return getCart(customerEmail, guestToken);
    }

    // -------------------- DELETE /cart/items/{itemId} --------------------

    @Override
    @Transactional
    public CartResponseDto deleteItem(String customerEmail, String guestToken, Long itemId) {
        Cart cart = findExistingCart(customerEmail, guestToken)
                .orElseThrow(() -> new CartNotFoundException(cartIdentifier(customerEmail, guestToken)));

        CartItem item = cartItemRepository.findByIdAndCartId_Id(itemId, cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException(itemId));

        cartItemRepository.delete(item);
        return getCart(customerEmail, guestToken);
    }

    // -------------------- DELETE /cart --------------------

    @Override
    @Transactional
    public void clearCart(String customerEmail, String guestToken) {
        Cart cart = findExistingCart(customerEmail, guestToken)
                .orElseThrow(() -> new CartNotFoundException(cartIdentifier(customerEmail, guestToken)));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // -------------------- Guest -> Customer merge --------------------

    @Override
    @Transactional
    public void mergeGuestCartIntoCustomer(String guestToken, Customer customer) {
        if (guestToken == null || guestToken.isBlank()) {
            return;
        }

        Cart guestCart = cartRepository.findByGuestTokenWithItems(guestToken).orElse(null);
        if (guestCart == null || guestCart.getItems().isEmpty()) {
            if (guestCart != null) {
                cartRepository.delete(guestCart);
            }
            return;
        }

        Cart customerCart = cartRepository.findByCustomerIdWithItems(customer.getId())
                .orElseGet(() -> createEmptyCart(customer, null, guestCart.getStoreId(), guestCart.getCompanyId()));

        for (CartItem guestItem : guestCart.getItems()) {
            var existingOpt = cartItemRepository.findByCartId_IdAndProductId_Id(
                    customerCart.getId(), guestItem.getProductId().getId());

            if (existingOpt.isPresent()) {
                CartItem existing = existingOpt.get();
                existing.setQuantity(existing.getQuantity() + guestItem.getQuantity());
                cartItemRepository.save(existing);
            } else {
                CartItem moved = CartItem.builder()
                        .cartId(customerCart)
                        .productId(guestItem.getProductId())
                        .quantity(guestItem.getQuantity())
                        .price(guestItem.getPrice())
                        .coupon(guestItem.getCoupon())
                        .build();
                cartItemRepository.save(moved);
            }
        }

        cartRepository.delete(guestCart);
    }

    // -------------------- Helpers --------------------

    private java.util.Optional<Cart> findExistingCart(String customerEmail, String guestToken) {
        System.out.println("Searching guest token = " + guestToken);
        if (customerEmail != null) {
            Customer customer = customerRepository.findByEmail(customerEmail)
                    .orElseThrow(() -> new CartNotFoundException(customerEmail));
            return cartRepository.findByCustomerIdWithItems(customer.getId());
        }
        if (guestToken != null) {
            return cartRepository.findByGuestTokenWithItems(guestToken);
        }
        System.out.println("Searching guest token = " + guestToken);
        return java.util.Optional.empty();
    }

    private Cart findOrCreateCart(String customerEmail, String guestToken) {

        if (customerEmail != null) {

            Customer customer = customerRepository.findByEmail(customerEmail)
                    .orElseThrow(() -> new CartNotFoundException(customerEmail));

            return cartRepository.findByCustomerId_Id(customer.getId())
                    .orElse(null);
        }

        return cartRepository.findByGuestToken(guestToken)
                .orElse(null);
    }

    private Cart createEmptyCart(Customer customer, String guestToken, Store store, Company company) {
        Cart cart = Cart.builder()
                .customerId(customer)
                .guestToken(guestToken)
                .startDate(LocalDateTime.now())
                .storeId(store)
                .companyId(company)
                .items(new java.util.ArrayList<>())
                .build();
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public CartResponseDto applyCoupon(String customerEmail, String guestToken, String couponCode) {

        Cart cart = findExistingCart(customerEmail, guestToken)
                .orElseThrow(() -> new CartNotFoundException(cartIdentifier(customerEmail, guestToken)));

        Coupon coupon = couponRepository.findByCodeIgnoreCaseAndCompanyId_Id(couponCode, cart.getCompanyId().getId())
                .orElseThrow(() -> new BusinessException(couponCode));

        validateCoupon(coupon);

        cart.setCouponId(coupon);
        cartRepository.save(cart);

        return getCart(customerEmail, guestToken);
    }

    @Override
    @Transactional
    public CartResponseDto removeCoupon(String customerEmail, String guestToken) {
        Cart cart = findExistingCart(customerEmail, guestToken)
                .orElseThrow(() -> new CartNotFoundException(cartIdentifier(customerEmail, guestToken)));

        cart.setCouponId(null);
        cartRepository.save(cart);

        return getCart(customerEmail, guestToken);
    }

    private void validateCoupon(Coupon coupon) {
        if (Boolean.FALSE.equals(coupon.getIsActive())) {
            throw new BusinessException("Coupon is not active");
        }

        LocalDate today = LocalDate.now();
        if (coupon.getStartDate() != null && today.isBefore(coupon.getStartDate())) {
            throw new BusinessException("Coupon is not valid yet");
        }
        if (coupon.getEndDate() != null && today.isAfter(coupon.getEndDate())) {
            throw new BusinessException("Coupon has expired");
        }
        if (coupon.getMaxUsed() != null && coupon.getUsed() != null
                && coupon.getUsed() >= coupon.getMaxUsed()) {
            throw new BusinessException("Coupon usage limit reached");
        }
        // maxUsePerUser محتاج تتبع استخدام لكل عميل — محتاج Entity زي CouponUsage
        // لو عايز أضيفها قولي
    }

    private void validateProductPurchasable(Product product, int quantity) {
        if (Boolean.FALSE.equals(product.getIsActive())) {
            throw new ProductNotActiveException(product.getId());
        }
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero");
        }
        ensureStock(product, quantity);
    }

    private void ensureStock(Product product, int requestedQuantity) {
        int available = product.getStock() == null ? 0 : product.getStock();
        if (requestedQuantity > available) {
            throw new InsufficientStockException(product.getId(), requestedQuantity, available);
        }
    }

    private String cartIdentifier(String customerEmail, String guestToken) {
        return customerEmail != null ? customerEmail : String.valueOf(guestToken);
    }

    // TODO: replace these three with your real tenant-resolution mechanism (subdomain, header, etc.)
    private Store resolveStoreFromTenantContext() {
        throw new UnsupportedOperationException("Wire this to your TenantContext / store resolution");
    }

    private Company resolveCompanyFromTenantContext() {
        throw new UnsupportedOperationException("Wire this to your TenantContext / company resolution");
    }

    private Store resolveStoreForCompany(Customer customer) {
        throw new UnsupportedOperationException("Wire this to your TenantContext / store resolution");
    }

    private Company resolveCompanyForCustomer(Customer customer) {
        return customer.getCompanyId();
    }

}
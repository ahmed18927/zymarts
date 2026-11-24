package com.example.SaaS.company.cart.mapper;

import com.example.SaaS.common.entity.Cart;
import com.example.SaaS.common.entity.CartItem;
import com.example.SaaS.common.entity.Coupon;
import com.example.SaaS.common.entity.Product;
import com.example.SaaS.company.cart.dto.CartItemResponseDto;
import com.example.SaaS.company.cart.dto.CartResponseDto;
import com.example.SaaS.company.cart.dto.ProductSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.Mapping;
import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "product", source = "productId", qualifiedByName = "toProductSummary")
    @Mapping(target = "itemId", source = "id")
    @Mapping(target = "couponId", source = "coupon.id")
    @Mapping(target = "subtotal", expression = "java(item.getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())))")
    CartItemResponseDto toItemDto(CartItem item);

    List<CartItemResponseDto> toItemDtoList(List<CartItem> items);

    @Named("toProductSummary")
    default ProductSummaryDto toProductSummary(Product product) {
        if (product == null) return null;
        String mainImage = product.getImages() == null ? null :
                product.getImages().stream()
                        .filter(img -> Boolean.TRUE.equals(img.getIsMain()))
                        .findFirst()
                        .map(img -> img.getMediaUrl())
                        .orElse(product.getImages().isEmpty() ? null : product.getImages().get(0).getMediaUrl());

        return new ProductSummaryDto(
                product.getId(),
                mainImage,
                product.getSellPrice(),
                product.getIsActive(),
                product.getStock()
        );
    }

//    default CartResponseDto toCartDto(Cart cart, List<CartItemResponseDto> items, BigDecimal discount) {
//        BigDecimal subtotal = items.stream()
//                .map(CartItemResponseDto::subtotal)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        BigDecimal effectiveDiscount = discount == null ? BigDecimal.ZERO : discount;
//        BigDecimal total = subtotal.subtract(effectiveDiscount);
//
//        return new CartResponseDto(
//                cart.getId(),
//                cart.getGuestToken(),
//                items,
//                subtotal,
//                discount, // left null if no discount service has run yet
//                total
//
//
//                );
//    }
default CartResponseDto toCartDto(Cart cart, List<CartItemResponseDto> items, Coupon coupon) {
    BigDecimal subtotal = items.stream()
            .map(CartItemResponseDto::subtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal discount = calculateDiscount(subtotal, coupon);
    BigDecimal total = subtotal.subtract(discount);

    return new CartResponseDto(
            cart.getId(),
            cart.getGuestToken(),
            items,
            subtotal,
            discount,
            total
    );
}

    // ⬇️ method جديدة لحساب الخصم
    default BigDecimal calculateDiscount(BigDecimal subtotal, Coupon coupon) {
        if (coupon == null || Boolean.FALSE.equals(coupon.getIsActive())) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount;
        if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
            discount = subtotal.multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100));
        } else { // FIXED
            discount = coupon.getDiscountValue();
        }

        return discount.min(subtotal); // الخصم ميتعديش قيمة السلة
    }

}

package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Table(
        name = "order_prices",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"order_id"})
        }
)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Order id is required")
    @JoinColumn(nullable = false, name = "order_id")
    private Order orderId;

    @NotNull(message = "Total price is required")
    @PositiveOrZero(message = "Total price must be greater than or equal to 0")
    @Column(nullable = false, name = "total_price")
    private BigDecimal totalPrice;

    @NotNull(message = "Subtotal is required")
    @PositiveOrZero(message = "Subtotal must be greater than or equal to 0")
    @Column(nullable = false, name = "sub_total")
    private BigDecimal  subTotal;

    @NotNull(message = "Discount is required")
    @PositiveOrZero(message = "Discount must be greater than or equal to 0")
    @Column(nullable = false, name = "discount")
    private BigDecimal  discount;

    @NotNull(message = "Shipping price is required")
    @PositiveOrZero(message = "Shipping price must be greater than or equal to 0")
    @Column(nullable = false, name = "shipping_price")
    private BigDecimal  shippingPrice;

    @PrePersist
    @PreUpdate
    public void validatePrices() {

        if (subTotal == null) subTotal = BigDecimal.ZERO;
        if (discount == null) discount = BigDecimal.ZERO;
        if (shippingPrice == null) shippingPrice = BigDecimal.ZERO;

        if (discount.compareTo(subTotal) > 0) {
            discount = subTotal;
        }

        totalPrice = subTotal.subtract(discount).add(shippingPrice);

        if (totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            totalPrice = BigDecimal.ZERO;
        }
    }
}

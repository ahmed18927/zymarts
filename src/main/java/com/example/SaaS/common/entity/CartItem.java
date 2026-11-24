package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "cart_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "product_id"})
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Cart id is required")
//    @Positive(message = "Cart id must be positive")
    @JoinColumn(nullable = false, name = "cart_id")
    private Cart cartId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Product id is required")
    @JoinColumn(nullable = false, name = "product_id")
    private Product productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100, message = "Quantity must not exceed 100")
    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @PrePersist
    @PreUpdate
    public void validateQuantity() {
        if (quantity == null || quantity < 1) {
            quantity = 1;
        }
    }

}
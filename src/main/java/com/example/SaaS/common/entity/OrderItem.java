package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Table(
        name = "order_items",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"order_id", "product_id"})
        }
)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Order id is required")
    @JoinColumn(nullable = false, name = "order_id")
    private Order orderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Product id is required")
    @JoinColumn(nullable = false, name = "product_id")
    private Product productId;

    @NotNull(message = "Price is required")
    @Column(nullable = false, name = "price")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal  price;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false, name = "qty")
    private Integer quantity;

    @NotNull(message = "Product data is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String productData;

    @NotNull(message = "Total price is required")
    @Positive(message = "Total price must be greater than 0")
    @Column(nullable = false, name = "total_price")
    private BigDecimal totalPrice;

    @PrePersist
    @PreUpdate
    public void calculateTotalPrice() {
        if (price != null && quantity != null) {
            this.totalPrice = price.multiply(BigDecimal.valueOf(quantity));
        }
    }
}

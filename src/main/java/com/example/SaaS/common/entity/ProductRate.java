package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(
        name = "product_rates",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"rate_id", "product_id"})
        },
        indexes = {
                @Index(name = "idx_product_rates_product", columnList = "product_id"),
                @Index(name = "idx_product_rates_rate", columnList = "rate_id")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Rate is required")
    @JoinColumn(nullable = false)
    private Rate rateId;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @NotNull(message = "Product is required")
//    @JoinColumn(nullable = false)
//    private Product productId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(nullable = false)
    private Integer rating;

    @Size(max = 500, message = "Comment must not exceed 500 characters")
    @Column(columnDefinition = "TEXT")
    private String comment;
}

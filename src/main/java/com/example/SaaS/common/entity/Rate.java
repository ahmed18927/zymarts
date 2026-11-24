package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Table(
        name = "rates",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"customer_id", "product_id", "order_id"})
        },
        indexes = {
                @Index(name = "idx_rates_product", columnList = "product_id"),
                @Index(name = "idx_rates_customer", columnList = "customer_id"),
                @Index(name = "idx_rates_company", columnList = "company_id")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // Relations IDs
    // =========================

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @NotNull(message = "Product is required")
//    @JoinColumn(nullable = false,name = "product_id")
//    private Product productId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Customer is required")
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Company is required")
    @JoinColumn(name = "company_id",nullable = false)
    private Company companyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Order is required")
    @JoinColumn(name = "order_id",nullable = false)
    private Order orderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Store is required")
    @JoinColumn(name = "store_id",nullable = false)
    private Store storeId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(nullable = false)
    private Integer rating;

    @Size(max = 500, message = "Comment must not exceed 500 characters")
    @Column(columnDefinition = "TEXT")
    private String comment;

    @OneToMany(mappedBy = "rateId")
    private List<ProductRate> productRates;

}

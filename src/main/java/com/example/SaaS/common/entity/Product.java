package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String name;

    @Column(name = "sell_price")
    private BigDecimal sellPrice;

    private Integer stock;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "is_active")
    private Boolean isActive = false;

    private Integer ordering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    private Employee addedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store storeId;

    @Column(name = "avg_rate")
    private BigDecimal avgRate;

    @Column(name = "buying_price")
    private BigDecimal buyingPrice;

    @OneToOne(mappedBy = "productId", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductDetails productDetails;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductTranslation> translations;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    @OneToMany(mappedBy = "productId")
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "productId")
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "productId")
    private List<OrderItem> orderItems;

//    @OneToMany(mappedBy = "productId")
//    private List<ProductRate> productRates;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductRate> productRates;

//    @OneToMany(mappedBy = "productId")
//    private List<Rate> rates;

}
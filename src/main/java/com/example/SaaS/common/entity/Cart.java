package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_id")
    private Customer customerId;

    @Column(name = "guest_token", unique = true)
    private String guestToken;

    @NotNull(message = "Start date is required")
    @PastOrPresent(message = "Start date must be in the past or present")
    @Column(nullable = false, name = "start_date")
    private LocalDateTime startDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @NotNull(message = "store id is required")
    @JoinColumn(nullable = false, name = "store_id")
    private Store storeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "coupon_id")
    private Coupon couponId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull(message = "company id is required")
    private Company companyId;

    @CreationTimestamp
    @PastOrPresent
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @PastOrPresent
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        if (startDate == null) startDate = LocalDateTime.now();
        if (createdAt == null) createdAt = Instant.now();
        if (updatedAt == null) updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    @OneToMany(mappedBy = "cartId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItem> items = new java.util.ArrayList<>();
}
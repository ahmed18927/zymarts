package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Table(name = "orders")
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @NotNull(message = "customer id  is required")
    @JoinColumn(nullable = false, name = "customer_id")
    private Customer customerId;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @NotNull(message = "address id is required")
    @JoinColumn(nullable = false, name = "address_id")
    private Address addressId;

    @NotBlank(message = "status is required")
    @Column(nullable = false, name = "status")
    @Pattern(
            regexp = "PENDING|PAID|SHIPPED|CANCELLED|COMPLETED",
            message = "Status must be PENDING, PAID, SHIPPED, CANCELLED, or COMPLETED"
    )
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull(message = "company id is required")
    private Company companyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "store_id")
    private Store storeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "coupon_id")
    private Coupon couponId;

    @Column(name = "coupon_data",columnDefinition = "TEXT")
    private String couponData;

    @CreationTimestamp
    @PastOrPresent(message = "Created date must be in the past or present")
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @PastOrPresent(message = "Updated date must be in the past or present")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();

        if (this.status == null) {
            this.status = "PENDING";
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }
    @OneToOne(mappedBy = "orderId", cascade = CascadeType.ALL)
    private OrderPrice orderPrice;

    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @OneToMany(mappedBy = "orderId")
    private List<Rate> rates;

}

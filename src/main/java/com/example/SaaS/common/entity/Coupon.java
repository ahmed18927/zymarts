package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Column(name = "discount_type")
    private String discountType;

    private BigDecimal discountValue;
    @Column(name = "max_used")
    private Integer maxUsed;
    private Integer used;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private String image;

    @Column(name = "is_free_shipping")
    private Boolean isFreeShipping = false;

    @Column(name = "max_use_per_user")
    private Integer maxUsePerUser;

    @Column(name = "applied_on_discount")
    private Boolean appliedOnDiscount = false;

    @OneToMany(mappedBy = "couponId")
    private List<Cart> carts;

    @OneToMany(mappedBy = "couponId")
    private List<Order> orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="company_id")
    private Company company;

}
package com.example.SaaS.common.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String name;
    @Column(nullable = true)
    private String image;

    @Column(name = "is_active")
    private Boolean isActive = false;

    private Integer ordering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    private Employee addedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    private String slug;

    @OneToMany(mappedBy = "category" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryTransaction> translations = new ArrayList<>();

    @OneToMany(mappedBy = "categoryId", cascade = CascadeType.ALL)
    private java.util.List<SubCategory> subCategories;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}
package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import com.example.SaaS.common.converter.JsonMapConverter;
import java.util.Map;

@Entity
@Table(name = "companies")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String domain;
    private String logo;
    @Column(name ="is_active")
    private Boolean isActive = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private Package packageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    private Admin addedBy;

    @OneToMany(mappedBy = "companyId", cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Category> categories;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "companyId")
    private List<Cart> carts;

    @OneToMany(mappedBy = "companyId")
    private List<Order> orders;

    @OneToMany(mappedBy = "companyId")
    private List<Rate> rates;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme themeId;

    @Convert(converter = JsonMapConverter.class)
    @Column(name = "custom_theme_settings", columnDefinition = "TEXT")
    private Map<String, String> customThemeSettings;

}

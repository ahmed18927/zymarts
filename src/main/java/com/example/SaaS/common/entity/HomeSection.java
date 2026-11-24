package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "home_sections")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // المحتوى الإنجليزي (default)
    @Column(nullable = false)
    private String heroTitleEn;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String heroSubtitleEn;

    @Column(nullable = false)
    private String ctaPrimaryTextEn;

    @Column(nullable = false)
    private String ctaSecondaryTextEn;

    // روابط الـ CTA
    private String ctaPrimaryLink;
    private String ctaSecondaryLink;

    // Stats
    private Integer totalClients;
    private Integer totalOrders;
    private Integer totalProducts;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "homeSection", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HomeSectionTranslation> translations = new ArrayList<>();
}
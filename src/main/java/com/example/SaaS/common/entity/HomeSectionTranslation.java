package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "home_section_translations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"home_section_id", "lang_code"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeSectionTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_section_id", nullable = false)
    private HomeSection homeSection;

    @Column(nullable = false, length = 10)
    private String langCode;  // "en", "ar"

    private String heroTitle;

    @Column(columnDefinition = "TEXT")
    private String heroSubtitle;

    private String ctaPrimaryText;
    private String ctaSecondaryText;
}

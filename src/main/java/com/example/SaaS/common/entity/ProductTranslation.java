package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_translations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "lang_code", length = 2)
    private String langCode;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "specifications", columnDefinition = "TEXT")
    private String specifications;


}

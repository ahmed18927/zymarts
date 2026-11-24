package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "features")
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private String slug;
    private Integer ordering;
    private String image;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    private Admin addedBy;

    @OneToMany(mappedBy = "feature" ,cascade = CascadeType.ALL)
    private List<FeatureTranslation> translations = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();


}

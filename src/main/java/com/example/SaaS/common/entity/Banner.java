package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name ="banners")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String link;
    private Integer ordering;
    @Column(name ="is_active")
    private Boolean isActive= false;
    @Column(name ="start_date")
    private LocalDate startDate;
    @Column(name ="end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="company_id")
    private Company companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="category_id")
    private Category categoryId;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}

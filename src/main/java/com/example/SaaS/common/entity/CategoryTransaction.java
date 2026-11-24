package com.example.SaaS.common.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "category_transactions" ,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"category_id" ,"locale"})
        }
)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String name;
    private String description;

    private String locale;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL )
    @JoinColumn(name ="category_id")
    private Category category;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}

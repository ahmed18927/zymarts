package com.example.SaaS.common.entity;

import com.example.SaaS.common.converter.JsonMapConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "countries")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter =  JsonMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, String> name;
    private String code;
    private String flag;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY , orphanRemoval = true , mappedBy = "country")
    private List<City> cities = new ArrayList<>();
    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}

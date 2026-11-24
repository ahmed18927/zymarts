package com.example.SaaS.common.entity;

import com.example.SaaS.common.converter.JsonMapConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "themes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Convert(converter = JsonMapConverter.class)
    @Column(name = "css_variables", columnDefinition = "TEXT")
    private Map<String, String> cssVariables;
    private String layout;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}

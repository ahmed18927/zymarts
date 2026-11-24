package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "packages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotBlank(message = "Package name is required")
//    @Size(max = 100, message = "Package name must be less than 100 characters")
//    @Column(nullable = false)
//    private String name;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be zero or positive")
    @Column(nullable = false)
    private BigDecimal price;

//    @NotNull(message = "Max products is required")
//    @Min(value = 1, message = "Max products must be at least 1")
    @Column(nullable = true)
    private Integer maxProducts;

    @Size(max = 1000, message = "Features must not exceed 1000 characters")
    @Column(nullable = true ,columnDefinition = "TEXT")
    private String features;

//    @NotNull(message = "Max orders is required")
//    @Min(value = 1, message = "Max orders must be at least 1")
    @Column(nullable = true)
    private Integer maxOrder;

    @NotNull
    @Column(nullable = false)
    private Boolean isActive = true;

    @NotNull(message = "Ordering is required")
    @Min(value = 0, message = "Ordering must be zero or positive")
    @Column(nullable = false)
    private Integer ordering = 0;

//    @NotNull(message = "Duration is required")
//    @Min(value = 1, message = "Duration must be at least 1 day")
    @Column(nullable = false)
    private Integer duration;

//    @NotNull(message = "Added by is required")
    @Column(nullable = false)
    private Long addedBy;

    @OneToMany(mappedBy = "packageId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageRole> packageRoles;

    @OneToMany(
            mappedBy = "pkg" ,
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private List<PackageTranslation> translations = new ArrayList<>();

}

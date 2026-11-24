package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "package_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Role name is required")
    @Size(max = 100, message = "Role name must be less than 100 characters")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "AddedBy is required")
    @Column(nullable = false)
    private Long addedBy;

    @NotNull
    @Column(nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "packageRoleId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageRole> packageRoles;

}


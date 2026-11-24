package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "package_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackagePermissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Permission name is required")
    @Size(max = 100, message = "Permission name must be less than 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Route name is required")
    @Size(max = 200, message = "Route name must be less than 200 characters")
    @Column(nullable = false)
    private String routeName;

    @OneToMany(mappedBy = "packagePermissionId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageRolePermission> packageRolePermissions;

}

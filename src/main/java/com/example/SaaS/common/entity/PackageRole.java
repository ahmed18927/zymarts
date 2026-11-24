package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "package_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @NotNull(message = "PackageId is required")
    @JoinColumn(name = "package_id",nullable = false)
    private Package packageId;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @NotNull(message = "PackageRoleId is required")
    @JoinColumn(name = "package_role_id",nullable = false)
    private PackageRoles packageRoleId;

    @OneToMany(mappedBy = "packageRole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageRolePermission> permissions;

}


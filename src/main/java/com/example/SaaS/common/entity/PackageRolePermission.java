package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "package_role_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageRolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "PackageRoleId is required")
    @JoinColumn(name = "package_role_id", nullable = false)
    private PackageRole packageRole;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "PermissionId is required")
    @JoinColumn(name = "package_permission_id", nullable = false)
    private PackagePermissions packagePermissionId;
}


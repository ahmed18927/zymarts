package com.example.SaaS.superAdmin.packagee;

import com.example.SaaS.common.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package,Long> {
    /**
     * البحث عن الباكدجات النشطة فقط
     */
    List<Package> findByIsActiveTrue();

    /**
     * البحث عن باكدج نشط بواسطة ID
     */
    Optional<Package> findByIdAndIsActiveTrue(Long id);

    /**
     * البحث بواسطة الـ ordering
     */
    List<Package> findAllByOrderByOrderingAsc();

    /**
     * البحث عن الباكدجات النشطة مرتبة
     */
    List<Package> findByIsActiveTrueOrderByOrderingAsc();
}

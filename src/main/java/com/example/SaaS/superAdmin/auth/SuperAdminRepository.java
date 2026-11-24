package com.example.SaaS.superAdmin.auth;

import com.example.SaaS.common.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuperAdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
}

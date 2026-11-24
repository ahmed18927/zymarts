package com.example.SaaS.company.theme;

import com.example.SaaS.common.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findByIsActiveTrue();
}

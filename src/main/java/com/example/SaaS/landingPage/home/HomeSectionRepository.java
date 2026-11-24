package com.example.SaaS.landingPage.home;

import com.example.SaaS.common.entity.HomeSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HomeSectionRepository extends JpaRepository<HomeSection, Long> {
    Optional<HomeSection> findFirstByIsActiveTrueOrderByIdDesc();
}

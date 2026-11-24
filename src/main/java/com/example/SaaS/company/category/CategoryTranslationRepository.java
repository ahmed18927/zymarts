package com.example.SaaS.company.category;

import com.example.SaaS.common.entity.CategoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryTranslationRepository extends JpaRepository<CategoryTransaction , Long> {
}

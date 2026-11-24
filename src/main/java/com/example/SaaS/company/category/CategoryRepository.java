package com.example.SaaS.company.category;

import com.example.SaaS.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findTop10ByOrderByIdDesc();

    Page<Category> findByCompanyId(Long companyId , Pageable pageable);
    Optional<Category> findByIdAndCompanyId(Long id, Long companyId);
}

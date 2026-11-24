package com.example.SaaS.company.subCategory;

import com.example.SaaS.common.entity.Category;
import com.example.SaaS.common.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {
//    List<SubCategory> findByCompanyId(Long companyId);
//    Optional<SubCategory> findByIdAndCompanyId(Long id, Long companyId);
//Page<SubCategory> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("""
    SELECT s FROM SubCategory s
    WHERE s.categoryId.id = :categoryId
    """)
    Page<SubCategory> findByCategoryId(
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

}

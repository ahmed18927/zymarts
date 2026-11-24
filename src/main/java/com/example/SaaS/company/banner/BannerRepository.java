package com.example.SaaS.company.banner;

import com.example.SaaS.common.entity.Banner;
import com.example.SaaS.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findTop6ByOrderByCreatedAtDesc();

    //    Page<Banner> findByCompanyId(@Param("companyId") Long companyId,  Pageable  pageable);
    @Query("""
            SELECT b FROM Banner b
            WHERE b.companyId.id = :companyId
            """)
    Page<Banner> findByCompanyId(
            @Param("companyId") Long companyId,
            Pageable pageable
    );

    Optional<Banner> findByIdAndCompanyId(Long id, Long companyId);
}

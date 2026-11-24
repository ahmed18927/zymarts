package com.example.SaaS.company.product;

import com.example.SaaS.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("""
    SELECT DISTINCT p 
    FROM Product p 
    LEFT JOIN FETCH p.translations 
    WHERE p.isActive = true
    ORDER BY p.id DESC
""")
    List<Product> findTop10ByIsActiveTrue();


    @Query("""
        SELECT DISTINCT p 
        FROM Product p 
        LEFT JOIN FETCH p.translations 
        WHERE p.isActive = true 
          AND p.companyId.id = :companyId
        ORDER BY p.id DESC
    """)
    List<Product> findAllActiveByCompanyId(@Param("companyId") Long companyId);

    /* ============ PUBLIC: منتج واحد بتاع شركة معينة ============ */
    @Query("""
        SELECT p 
        FROM Product p 
        LEFT JOIN FETCH p.translations 
        WHERE p.isActive = true 
          AND p.id = :id 
          AND p.companyId.id = :companyId
    """)
    Optional<Product> findActiveByIdAndCompanyId(@Param("id") Long id, @Param("companyId") Long companyId);
}

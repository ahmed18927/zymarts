package com.example.SaaS.company.favorite;

import com.example.SaaS.common.entity.Customer;
import com.example.SaaS.common.entity.Favorite;
import com.example.SaaS.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Page<Favorite> findByCustomerId_Id(Long customerId, Pageable pageable);

    Optional<Favorite> findByCustomerId_IdAndProductId_Id(
            Long customerId,
            Long productId
    );

    boolean existsByCustomerId_IdAndProductId_Id(
            Long customerId,
            Long productId
    );

    void deleteByCustomerId_IdAndProductId_Id(
            Long customerId,
            Long productId
    );

}

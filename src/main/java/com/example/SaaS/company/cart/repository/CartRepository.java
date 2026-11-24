package com.example.SaaS.company.cart.repository;

import com.example.SaaS.common.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long>{

    @EntityGraph(attributePaths = {"items", "items.productId", "items.productId.images"})
    @Query("SELECT c FROM Cart c WHERE c.customerId.id = :customerId")
    Optional<Cart> findByCustomerIdWithItems(@Param("customerId") Long customerId);

    @EntityGraph(attributePaths = {"items", "items.productId", "items.productId.images"})
    @Query("SELECT c FROM Cart c WHERE c.guestToken = :guestToken")
    Optional<Cart> findByGuestTokenWithItems(@Param("guestToken") String guestToken);

    Optional<Cart> findByCustomerId_Id(Long customerId);

    Optional<Cart> findByGuestToken(String guestToken);

    @Query("SELECT c FROM Cart c WHERE c.customerId.id = :customerId")
    Optional<Cart> findByCustomerAndStore(@Param("customerId") Long customerId);
}


package com.example.SaaS.company.cart.repository;

import com.example.SaaS.common.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartId_IdAndProductId_Id(Long cartId, Long productId);

    Optional<CartItem> findByIdAndCartId_Id(Long itemId, Long cartId);

    @org.springframework.data.jpa.repository.Query(
            "SELECT ci FROM CartItem ci " +
                    "JOIN FETCH ci.productId p " +
                    "LEFT JOIN FETCH p.images " +
                    "WHERE ci.cartId.id = :cartId"
    )
    List<CartItem> findAllByCartId(@org.springframework.data.repository.query.Param("cartId") Long cartId);
}
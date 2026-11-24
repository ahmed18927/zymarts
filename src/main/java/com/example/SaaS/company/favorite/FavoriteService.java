package com.example.SaaS.company.favorite;

import com.example.SaaS.common.entity.Customer;
import com.example.SaaS.common.entity.Favorite;
import com.example.SaaS.common.entity.Product;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.customer.CustomerRepository;
import com.example.SaaS.company.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    // TODO: replace with authenticated customer
    private Long getCurrentCustomerId() {
        return 1L;
    }

    public Favorite addToFavorite(Long productId) {

        Long customerId = getCurrentCustomerId();

        if (favoriteRepository.existsByCustomerId_IdAndProductId_Id(
                customerId,
                productId
        )) {
            throw new BusinessException("Product already in favorite");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new BusinessException("Product not found"));

        Customer customer = customerRepository.getReferenceById(customerId);

        Favorite favorite = Favorite.builder()
                .productId(product)
                .customerId(customer)
                .build();

        return favoriteRepository.save(favorite);
    }

    public void removeFromFavorite(Long productId) {

        Long customerId = getCurrentCustomerId();

        favoriteRepository.deleteByCustomerId_IdAndProductId_Id(
                customerId,
                productId
        );
    }
    public Page<Favorite> getFavorites(Pageable pageable) {

        Long customerId = getCurrentCustomerId();

        return favoriteRepository.findByCustomerId_Id(
                customerId,
                pageable
        );
    }
}
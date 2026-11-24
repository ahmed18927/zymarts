package com.example.SaaS.company.product;

import com.example.SaaS.common.entity.Product;
import com.example.SaaS.common.entity.ProductTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTranslationRepository extends JpaRepository<ProductTranslation, Long> {

    Optional<ProductTranslation> findByProductAndLangCode(Product product, String langCode);

}


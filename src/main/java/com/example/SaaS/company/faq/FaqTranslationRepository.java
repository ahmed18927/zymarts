package com.example.SaaS.company.faq;

import com.example.SaaS.common.entity.Faq;
import com.example.SaaS.common.entity.FaqTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaqTranslationRepository extends JpaRepository<FaqTranslation, Long> {
    Optional<FaqTranslation> findByFaqAndLangCode(Faq faq, String langCode);
}

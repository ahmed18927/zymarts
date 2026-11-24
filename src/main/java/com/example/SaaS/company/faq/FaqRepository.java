package com.example.SaaS.company.faq;

import com.example.SaaS.common.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.SaaS.common.entity.Company;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<Faq,Long> {

    List<Faq> findByCompanyOrderByOrderingAsc(Company company);
    List<Faq> findByIsActiveTrueOrderByOrderingAsc();
    List<Faq> findByCompanyAndIsActiveTrueOrderByOrderingAsc(Company company);
}

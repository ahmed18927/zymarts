package com.example.SaaS.superAdmin.country;

import com.example.SaaS.common.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {

    boolean existsByCode(String code);

}

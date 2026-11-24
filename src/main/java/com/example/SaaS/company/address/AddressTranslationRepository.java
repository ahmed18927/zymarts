package com.example.SaaS.company.address;

import com.example.SaaS.common.entity.Address;
import com.example.SaaS.common.entity.AddressTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressTranslationRepository extends JpaRepository<AddressTranslation, Long> {
    Optional<AddressTranslation> findByAddressAndLangCode(Address address, String langCode);
}

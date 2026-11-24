package com.example.SaaS.superAdmin.country;

import com.example.SaaS.common.entity.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CountryMapper {
    public CountryResponseDTO toResponse(Country country, String resolvedName) {
        return CountryResponseDTO.builder()
                .id(country.getId())
                .name(resolvedName)
                .code(country.getCode())
                .flag(country.getFlag())
                .build();
    }
}

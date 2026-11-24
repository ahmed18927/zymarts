package com.example.SaaS.superAdmin.country;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryResponseDTO {
    private Long id;
    private String name;
    private String code;
    private String flag;
}

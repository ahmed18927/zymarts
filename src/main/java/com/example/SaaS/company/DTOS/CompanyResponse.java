package com.example.SaaS.company.DTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {
    private Long id;
    private String name;
    private String domain;
    private String logo;
    private Boolean isActive;
    private String addedBy;
    private Instant createdAt;
    private Instant updatedAt;
}

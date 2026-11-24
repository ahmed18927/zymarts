package com.example.SaaS.company.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeResourceDTO {
    private String token;

    private Long id;
    private String name;
    private String email;
    private Long companyId;
    private String companyName;
    private String domain;
    private String role;
    private Boolean isMain;
}

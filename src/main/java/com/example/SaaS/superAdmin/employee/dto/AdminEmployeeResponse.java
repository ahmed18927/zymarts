package com.example.SaaS.superAdmin.employee.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminEmployeeResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private Boolean isMain;
    private String image;
    private Long companyId;
    private String companyName;
}

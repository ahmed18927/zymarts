package com.example.SaaS.company.employee;

import lombok.*;

@Data
@Builder
public class EmployeeResourceDTO {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String isMain;
    private String image;
}


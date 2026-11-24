package com.example.SaaS.company.customer.DTOS;

import lombok.Data;

@Data
public class CustomerRegisterRequest {
    private Long   companyId;
    private String name;
    private String phone;
    private String email;
    private String password;
}

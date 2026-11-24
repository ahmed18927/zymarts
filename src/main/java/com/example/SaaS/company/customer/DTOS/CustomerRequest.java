package com.example.SaaS.company.customer.DTOS;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerRequest {
    private Long   companyId;
    private String name;
    private String phone;
    private String email;
    private String password;
}
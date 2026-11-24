package com.example.SaaS.company.customer.DTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLoginRequest {
    private Long   companyId;
    private String name;
    private String phone;
    private String email;
    private String password;
}
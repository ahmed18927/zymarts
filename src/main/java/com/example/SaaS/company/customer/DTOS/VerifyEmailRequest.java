package com.example.SaaS.company.customer.DTOS;

import lombok.Data;

@Data
public class VerifyEmailRequest {
    private String email;
    private String code;
}

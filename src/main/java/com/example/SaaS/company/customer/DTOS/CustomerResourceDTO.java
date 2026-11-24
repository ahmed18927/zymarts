package com.example.SaaS.company.customer.DTOS;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResourceDTO {
    private String token;
    private Long   id;
    private String name;
    private String email;
    private String phone;
}

package com.example.SaaS.company.customer.DTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse{

    private Long id;
    private Long companyId;
    private String name;
    private String phone;
    private String email;
    private String image;
    private LocalDateTime verifyEmail;
    private LocalDateTime verifyPhone;

}

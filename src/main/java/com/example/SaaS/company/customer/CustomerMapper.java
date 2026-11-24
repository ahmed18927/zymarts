package com.example.SaaS.company.customer;

import com.example.SaaS.common.entity.Customer;
import com.example.SaaS.company.customer.DTOS.CustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerResponse toResponse(Customer customer) {
        if (customer == null){
            return null;
        }

        return CustomerResponse.builder()
                .id(customer.getId())
                .companyId(customer.getCompanyId() != null ? customer.getCompanyId().getId() : null)
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .image(customer.getImage())
                .verifyEmail(customer.getVerifyEmail())
                .verifyPhone(customer.getVerifyPhone())
                .build();
    }
}

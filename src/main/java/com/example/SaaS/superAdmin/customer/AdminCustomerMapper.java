package com.example.SaaS.superAdmin.customer;

import com.example.SaaS.common.entity.Customer;
import com.example.SaaS.superAdmin.customer.dto.AdminCustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class AdminCustomerMapper {

    public AdminCustomerResponse toResponse(Customer customer) {
        if (customer == null) {
            return null;
        }

        return AdminCustomerResponse.builder()
                .id(customer.getId())
                .companyId(customer.getCompanyId() != null ? customer.getCompanyId().getId() : null)
                .companyName(customer.getCompanyId() != null ? customer.getCompanyId().getName() : null)
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .image(customer.getImage())
                .verifyEmail(customer.getVerifyEmail())
                .verifyPhone(customer.getVerifyPhone())
                .build();
    }
}

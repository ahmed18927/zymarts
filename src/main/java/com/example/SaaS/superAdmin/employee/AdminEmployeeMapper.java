package com.example.SaaS.superAdmin.employee;

import com.example.SaaS.common.entity.Employee;
import com.example.SaaS.superAdmin.employee.dto.AdminEmployeeResponse;
import org.springframework.stereotype.Component;

@Component
public class AdminEmployeeMapper {

    public AdminEmployeeResponse toResponse(Employee employee) {
        if (employee == null) {
            return null;
        }

        return AdminEmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .phone(employee.getPhone())
                .email(employee.getEmail())
                .isMain(employee.getIsMain())
                .image(employee.getImage())
                .companyId(employee.getCompany() != null ? employee.getCompany().getId() : null)
                .companyName(employee.getCompany() != null ? employee.getCompany().getName() : null)
                .build();
    }
}

package com.example.SaaS.company.employee;

import com.example.SaaS.common.entity.Employee;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Builder
@Component
public class EmployeeMapper {
    public EmployeeResourceDTO toResource(Employee employee) {

        return EmployeeResourceDTO.builder()
                .id(employee.getId())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .name(employee.getName())
//                .isMain(employee.getIsMain())
                .build();
    }
}

package com.example.SaaS.company.auth;

import com.example.SaaS.common.entity.Employee;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.employee.EmployeeRepository;
import com.example.SaaS.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeAuthService {

    private final EmployeeRepository employeeRepository; // final for injection
    private final JwtUtil jwtUtil;                       // final for injection
    private final PasswordEncoder passwordEncoder;       // final for injection

    public EmployeeResourceDTO login(EmployeeLoginRequest request) {
        // Find employee by email
        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Email not found"));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new BusinessException("Invalid password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(employee.getEmail(), "EMPLOYEE");

        // Build response DTO
        return EmployeeResourceDTO.builder()
                .token(token)
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .companyId(employee.getCompany() != null ? employee.getCompany().getId() : null)
                .companyName(employee.getCompany() != null ? employee.getCompany().getName() : null)
                .domain(employee.getCompany() != null ? employee.getCompany().getDomain() : null)
                .role(employee.getRole() != null ? employee.getRole().name() : "EMPLOYEE")
                .isMain(employee.getIsMain())
                .build();
    }
}
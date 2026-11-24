package com.example.SaaS.superAdmin.employee;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Employee;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.employee.EmployeeRepository;
import com.example.SaaS.superAdmin.employee.dto.AdminEmployeeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdminEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public Page<Employee> getAllEmployees(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return employeeRepository.findAll(pageable);
        }
        return employeeRepository.findAll(pageable);
    }

    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException("Employee not found"));
    }

    public Employee createEmployee(AdminEmployeeRequest request, MultipartFile image) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new BusinessException("Company not found with ID: " + request.getCompanyId()));

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        if (employeeRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("Phone already exists");
        }

        Employee employee = Employee.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .company(company)
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .isMain(false)
                .build();

        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.saveFile(image);
            employee.setImage(imagePath);
        }

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long employeeId, AdminEmployeeRequest request, MultipartFile image) {
        Employee dbEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException("Employee not found"));

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new BusinessException("Company not found with ID: " + request.getCompanyId()));
            dbEmployee.setCompany(company);
        }

        if (request.getName() != null)
            dbEmployee.setName(request.getName());
        if (request.getEmail() != null)
            dbEmployee.setEmail(request.getEmail());
        if (request.getPhone() != null)
            dbEmployee.setPhone(request.getPhone());
        if (request.getPassword() != null && !request.getPassword().isEmpty())
            dbEmployee.setPassword(passwordEncoder.encode(request.getPassword()));

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.saveFile(image);
            dbEmployee.setImage(imageUrl);
        }

        return employeeRepository.save(dbEmployee);
    }

    public void deleteEmployee(Long employeeId) {
        Employee employee = getEmployeeById(employeeId);
        employeeRepository.delete(employee);
    }
}

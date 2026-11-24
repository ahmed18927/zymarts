package com.example.SaaS.company.employee;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Employee;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.security.EmployeeDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;
    private final CompanyRepository companyRepository;

    private Long getCurrentCompanyId() {
        return ((EmployeeDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getCompanyId();
    }

    public Employee findEmployeeById(Long id){
        return employeeRepository.findById(id).orElseThrow(
                ()->new BusinessException("employee not found")
        );
    }
    public Page<Employee> findAllEmployees(Pageable  pageable){

        Long companyId = getCurrentCompanyId();
        return employeeRepository.findByCompanyId(companyId ,pageable);
//        return employeeRepository.findAll();
    }
    public Employee deleteEmployee(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () ->new BusinessException("employee not found")
        );
        employeeRepository.delete(employee);
        return employee;
    }
    public Employee createEmployee(EmployeeRequest request){

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        if (employeeRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("Phone already exists");
        }

        if (employeeRepository.existsByName(request.getName())) {
            throw new BusinessException("Name already exists");
        }

        Long companyId = getCurrentCompanyId();
        Company company = companyRepository.getReferenceById(companyId);

        Employee employee = Employee.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .company(company) // assign Company entity

                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .isMain(false)
                .build();
        String imagePath = fileStorageService.saveFile( request.getImage());
        employee.setImage(imagePath);

        return employeeRepository.save(employee);
    }
    public Employee updateEmployee(Long id, EmployeeRequest request){
        Employee dbEmployee = employeeRepository.findById(id).orElseThrow(
                ()->new BusinessException("employee not found")
        );
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        if (employeeRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("Phone already exists");
        }

        if (employeeRepository.existsByName(request.getName())) {
            throw new BusinessException("Name already exists");
        }


        dbEmployee.setPassword(passwordEncoder.encode(request.getPassword()));
//        dbEmployee.setPassword(employee.getPassword());

        if (request.getName() != null)
            dbEmployee.setName(request.getName());
        if (request.getEmail() != null)
            dbEmployee.setEmail(request.getEmail());
        if (request.getPhone() != null)
            dbEmployee.setPhone(request.getPhone());
        if (request.getPassword() != null)
            dbEmployee.setPassword(passwordEncoder.encode(request.getPassword()));


        if (request.getImage() != null && !request.getImage().isEmpty()) {

            // optional: delete old image
            // fileStorageService.deleteFile(banner.getImageUrl());

            String imageUrl = fileStorageService.saveFile(request.getImage());
            dbEmployee.setImage(imageUrl);
        }
        employeeRepository.save(dbEmployee);
        return dbEmployee;
    }
}

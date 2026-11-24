package com.example.SaaS.seeder;

import com.example.SaaS.common.entity.Employee;
import com.example.SaaS.company.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (employeeRepository.count() > 0) {
            return;
        }

        Employee emp1 = new Employee();
        emp1.setName("John Doe");
        emp1.setEmail("john@example.com");
        emp1.setPassword(passwordEncoder.encode("Password123"));
        emp1.setPhone("12345678");

        Employee emp2 = new Employee();
        emp2.setName("Sarah Smith");
        emp2.setEmail("sarah@example.com");
        emp2.setPassword(passwordEncoder.encode("Sara12345"));
        emp2.setPhone("98765432");

        employeeRepository.saveAll(List.of(emp1, emp2));

        System.out.println("✅ Employees seeded successfully.");
    }
}

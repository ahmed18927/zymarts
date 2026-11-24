package com.example.SaaS.seeder;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Customer;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@Order(6)
@Component
@RequiredArgsConstructor
public class CustomerSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println("Running CustomerSeeder...");

        if (customerRepository.count() > 0) {
            System.out.println("Customers already exist. Skipping seeding.");
            return;
        }

        List<Company> companies = companyRepository.findAll();

        if (companies.isEmpty()) {
            System.out.println("No companies found. Skipping customer seeding.");
            return;
        }

        List<Customer> customers = new ArrayList<>();

        // Customers for Company 1
        if (companies.size() > 0) {
            Company company1 = companies.get(0);

            Customer customer1 = Customer.builder()
                    .companyId(company1)
                    .name("Ahmed Mohamed")
                    .phone("201012345678")
                    .email("ahmed.mohamed@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .image("customer1.jpg")
                    .verifyCode("123456")
                    .verifyEmail(LocalDateTime.now())
                    .verifyPhone(LocalDateTime.now())
                    .build();

            Customer customer2 = Customer.builder()
                    .companyId(company1)
                    .name("Sara Ali")
                    .phone("201098765432")
                    .email("sara.ali@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .image("customer2.jpg")
                    .verifyCode("654321")
                    .verifyEmail(LocalDateTime.now())
                    .verifyPhone(LocalDateTime.now())
                    .build();

            customers.add(customer1);
            customers.add(customer2);
        }

        // Customers for Company 2
        if (companies.size() > 1) {
            Company company2 = companies.get(1);

            Customer customer3 = Customer.builder()
                    .companyId(company2)
                    .name("Khaled Hassan")
                    .phone("966501234567")
                    .email("khaled.hassan@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .image("customer3.jpg")
                    .verifyCode("789012")
                    .verifyEmail(LocalDateTime.now())
                    .verifyPhone(LocalDateTime.now())
                    .build();

            Customer customer4 = Customer.builder()
                    .companyId(company2)
                    .name("Fatima Ahmed")
                    .phone("966509876543")
                    .email("fatima.ahmed@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .image(null) // No image
                    .verifyCode("210987")
                    .verifyEmail(LocalDateTime.now())
                    .verifyPhone(LocalDateTime.now())
                    .build();

            customers.add(customer3);
            customers.add(customer4);
        }

        // Customer for Company 3
        if (companies.size() > 2) {
            Company company3 = companies.get(2);

            Customer customer5 = Customer.builder()
                    .companyId(company3)
                    .name("Omar Abdullah")
                    .phone("971501234567")
                    .email("omar.abdullah@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .image("customer5.jpg")
                    .verifyCode("345678")
                    .verifyEmail(LocalDateTime.now())
                    .verifyPhone(LocalDateTime.now())
                    .build();

            customers.add(customer5);
        }

        customerRepository.saveAll(customers);

        System.out.println("Customers seeded successfully. Total: " + customers.size());
    }
}


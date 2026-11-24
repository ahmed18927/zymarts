package com.example.SaaS.seeder;

import com.example.SaaS.common.entity.Admin;
import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.repository.AdminRepository;
import com.example.SaaS.company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Order(3)
@Component
@RequiredArgsConstructor
public class CompanySeeder implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final AdminRepository adminRepository;


    @Override
    public void run(String... args) throws Exception {
        if (companyRepository.count() > 0) {
            System.out.println("Companies already exist. Skipping seeding.");
            return;
        }
        Admin admin = adminRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Instant now = Instant.now();

        Company company = Company.builder()
                .name("Tech Solutions Ltd")
                .domain("techsolutions.com")
                .logo("https://example.com/logos/techsolutions.png")
                .isActive(true)
                .addedBy(admin)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Company company2 = Company.builder()
                .name("Digital Marketing Pro")
                .domain("digitalmarketingpro.com")
                .logo("https://example.com/logos/digitalmarketing.png")
                .isActive(true)
                .addedBy(admin)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Company company3 = Company.builder()
                .name("E-Commerce Plus")
                .domain("ecommerceplus.com")
                .logo("https://example.com/logos/ecommerce.png")
                .isActive(false)
                .addedBy(admin)
                .createdAt(now)
                .updatedAt(now)
                .build();


        companyRepository.saveAll(List.of(company, company2, company3));

        System.out.println("Companies seeded successfully.");
    }
}

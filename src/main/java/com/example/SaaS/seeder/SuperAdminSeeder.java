package com.example.SaaS.seeder;


import com.example.SaaS.common.entity.Admin;
import com.example.SaaS.common.enums.Role;
import com.example.SaaS.superAdmin.auth.SuperAdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Order(1)
@Component
public class SuperAdminSeeder implements CommandLineRunner {

    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;

    public SuperAdminSeeder(SuperAdminRepository superAdminRepository,
                            PasswordEncoder passwordEncoder) {
        this.superAdminRepository = superAdminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        String adminEmail = "admin@example.com";
        String adminPassword = "Admin@123";

        // ✅ Check if admin already exists
        if (superAdminRepository.findByEmail(adminEmail).isPresent()) {
            System.out.println("SuperAdmin already exists. Skipping seeding.");
            return;
        }

        // ✅ Create SuperAdmin
        Admin admin = new Admin();
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);

        superAdminRepository.save(admin);

        System.out.println("✅ SuperAdmin seeded successfully");
        System.out.println("Email: " + adminEmail);
        System.out.println("Password: " + adminPassword);
    }
}

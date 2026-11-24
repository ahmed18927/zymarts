package com.example.SaaS.superAdmin.customer;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Customer;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.customer.CustomerRepository;
import com.example.SaaS.superAdmin.customer.dto.AdminCustomerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCustomerService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${upload-dir:uploads/customers}")
    private String uploadPath;

    public Page<Customer> getAllCustomers(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return customerRepository.findAll(pageable);
        }
        return customerRepository.findAll(pageable);
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("Customer not found"));
    }

    @Transactional
    public Customer createCustomer(AdminCustomerRequest request, MultipartFile imageFile) {
        log.info("Creating customer: {}", request.getEmail());

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new BusinessException("Company not found with ID: " + request.getCompanyId()));

        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Email already exists");
        }

        if (customerRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new BusinessException("Phone already exists");
        }

        String imagePath = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imagePath = saveImage(imageFile);
        }

        Customer customer = Customer.builder()
                .companyId(company)
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .image(imagePath)
                .verifyCode(generateVerifyCode())
                .build();

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long customerId, AdminCustomerRequest request, MultipartFile imageFile) {
        log.info("Updating customer ID: {}", customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("Customer not found"));

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new BusinessException("Company not found with ID: " + request.getCompanyId()));
            customer.setCompanyId(company);
        }

        customerRepository.findByEmail(request.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(customerId)) {
                        throw new BusinessException("Email already exists");
                    }
                });

        customerRepository.findByPhone(request.getPhone())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(customerId)) {
                        throw new BusinessException("Phone already exists");
                    }
                });

        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());

        if (imageFile != null && !imageFile.isEmpty()) {
            if (customer.getImage() != null) {
                deleteImage(customer.getImage());
            }
            customer.setImage(saveImage(imageFile));
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            customer.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long customerId) {
        Customer customer = getCustomerById(customerId);

        if (customer.getImage() != null) {
            deleteImage(customer.getImage());
        }

        customerRepository.delete(customer);
    }

    private String saveImage(MultipartFile file) {
        try {
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new BusinessException("Invalid file name");
            }

            String fileName = System.currentTimeMillis() + "_" + originalFilename;
            Path filePath = uploadDir.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {
            throw new BusinessException("Failed to save image: " + e.getMessage());
        }
    }

    private void deleteImage(String fileName) {
        try {
            Path filePath = Paths.get(uploadPath).resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Failed to delete image: {}", e.getMessage());
        }
    }

    private String generateVerifyCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}

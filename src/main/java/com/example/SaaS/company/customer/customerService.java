//package com.example.SaaS.company.customer;
//
//import com.example.SaaS.common.entity.Company;
//import com.example.SaaS.common.entity.Customer;
//import com.example.SaaS.common.exception.BusinessException;
//import com.example.SaaS.company.customer.DTOS.CustomerRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.List;
//import java.util.Random;
//
//@Service
//@RequiredArgsConstructor
//public class customerService {
//
//    private final CustomerRepository customerRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Value("${upload-dir:uploads/customers}")
//    private String uploadPath;
//
////    @Transactional
////    public Customer create (CustomerRequest request){
////
////        if (customerRepository.findByEmail(request.getEmail()).isPresent()){
////            throw new BusinessException("Email already exists");
////        }
////
////        if (customerRepository.findByPhone(request.getPhone()).isPresent()){
////            throw new BusinessException("Phone already exists");
////        }
////
////        Customer customer = Customer.builder()
////                .companyId(Company.builder().id(request.getCompanyId()).build())
////                .name(request.getName())
////                .phone(request.getPhone())
////                .email(request.getEmail())
////                .password(passwordEncoder.encode(request.getPassword()))
////                .image(request.getImage())
////                .verifyCode(generateVerifyCode())
////                .build();
////
////        return customerRepository.save(customer);
////    }
//@Transactional
//public Customer create(CustomerRequest request, MultipartFile imageFile) {
//
//    if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
//        throw new BusinessException("Email already exists");
//    }
//
//    if (customerRepository.findByPhone(request.getPhone()).isPresent()) {
//        throw new BusinessException("Phone already exists");
//    }
//
//    String imagePath = null;
//    if (imageFile != null && !imageFile.isEmpty()) {
//        imagePath = saveImage(imageFile);
//    }
//
//    Customer customer = Customer.builder()
//            .companyId(Company.builder().id(request.getCompanyId()).build())
//            .name(request.getName())
//            .phone(request.getPhone())
//            .email(request.getEmail())
//            .password(passwordEncoder.encode(request.getPassword()))
//            .image(imagePath)
//            .verifyCode(generateVerifyCode())
//            .build();
//
//    return customerRepository.save(customer);
//}
//
//    private String saveImage(MultipartFile file) {
//        try {
//            // إنشاء المجلد لو مش موجود
//            Path uploadDir = Paths.get(uploadPath);
//            if (!Files.exists(uploadDir)) {
//                Files.createDirectories(uploadDir);
//            }
//
//            // إنشاء اسم فريد للملف
//            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            Path filePath = uploadDir.resolve(fileName);
//
//            // حفظ الملف
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//            return fileName;
//
//        } catch (IOException e) {
//            throw new BusinessException("Failed to save image: " + e.getMessage());
//        }
//    }
//
////    @Transactional
////    public Customer update(Long id, CustomerRequest request){
////
////        Customer customer = customerRepository.findById(id)
////                .orElseThrow(() -> new BusinessException("Customer not found"));
////
////        customerRepository.findByEmail(request.getEmail())
////                .ifPresent(existing -> {
////                    if (!existing.getId().equals(id)){
////                        throw new BusinessException("Email already exists");
////                    }
////                });
////
////        customerRepository.findByPhone(request.getPhone())
////                .ifPresent(existing -> {
////                    if (!existing.getId().equals(id)) {
////                        throw new BusinessException("Phone already exists");
////                    }
////                });
////
////        customer.setName(request.getName());
////        customer.setPhone(request.getPhone());
////        customer.setEmail(request.getEmail());
////        customer.setImage(request.getImage());
////
////        if (request.getPassword() != null && !request.getPassword().isEmpty()){
////            customer.setPassword(passwordEncoder.encode(request.getPassword()));
////        }
////        return customerRepository.save(customer);
////    }
//
//    @Transactional
//    public Customer update(Long id, CustomerRequest request, MultipartFile imageFile) {
//
//        Customer customer = customerRepository.findById(id)
//                .orElseThrow(() -> new BusinessException("Customer not found"));
//
//        customerRepository.findByEmail(request.getEmail())
//                .ifPresent(existing -> {
//                    if (!existing.getId().equals(id)) {
//                        throw new BusinessException("Email already exists");
//                    }
//                });
//
//        customerRepository.findByPhone(request.getPhone())
//                .ifPresent(existing -> {
//                    if (!existing.getId().equals(id)) {
//                        throw new BusinessException("Phone already exists");
//                    }
//                });
//
//        customer.setName(request.getName());
//        customer.setPhone(request.getPhone());
//        customer.setEmail(request.getEmail());
//
//        if (imageFile != null && !imageFile.isEmpty()) {
//            // امسح الصورة القديمة لو موجودة
//            if (customer.getImage() != null) {
//                deleteImage(customer.getImage());
//            }
//            customer.setImage(saveImage(imageFile));
//        }
//
//        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
//            customer.setPassword(passwordEncoder.encode(request.getPassword()));
//        }
//
//        return customerRepository.save(customer);
//    }
//
//    private void deleteImage(String fileName) {
//        try {
//            Path filePath = Paths.get(uploadPath).resolve(fileName);
//            Files.deleteIfExists(filePath);
//        } catch (IOException e) {
//            // Log the error but don't throw exception
//            System.err.println("Failed to delete image: " + e.getMessage());
//        }
//    }
//
//    public  Customer getById(Long id){
//        return customerRepository.findById(id)
//                .orElseThrow(() -> new BusinessException("Customer not found"));
//    }
//
//    public List<Customer> getAllCustomers(){
//        return customerRepository.findAll();
//    }
//
//    public void delete(Long id){
//        Customer customer = getById(id);
//        customerRepository.delete(customer);
//    }
//
//    private String generateVerifyCode(){
//
//        return String.format("%06d", new Random().nextInt(999999));
//    }
//
//}
package com.example.SaaS.company.customer;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Customer;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.customer.DTOS.CustomerLoginRequest;
import com.example.SaaS.company.customer.DTOS.CustomerRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
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
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class customerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator; // ⬅️ أضف ده

    @Value("${upload-dir:uploads/customers}")
    private String uploadPath;

    @Transactional
    public Customer create(CustomerLoginRequest request, MultipartFile imageFile) {

        // ✅ Validate Request
        validateRequest(request);

        log.info("📥 Creating customer: {}", request.getEmail());

        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Email already exists");
        }

        if (customerRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new BusinessException("Phone already exists");
        }

        String imagePath = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            log.info("✅ Image is valid, saving...");
            imagePath = saveImage(imageFile);
            log.info("💾 Image saved as: {}", imagePath);
        }

        Customer customer = Customer.builder()
                .companyId(Company.builder().id(request.getCompanyId()).build())
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .image(imagePath)
                .verifyCode(generateVerifyCode())
                .build();

        Customer saved = customerRepository.save(customer);
        log.info("✅ Customer saved with ID: {}", saved.getId());

        return saved;
    }

    @Transactional
    public Customer update(Long id, CustomerLoginRequest request, MultipartFile imageFile) {

        // ✅ Validate Request
        validateRequestForUpdate(request);

        log.info("🔄 Updating customer ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Customer not found"));

        customerRepository.findByEmail(request.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new BusinessException("Email already exists");
                    }
                });

        customerRepository.findByPhone(request.getPhone())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
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

        Customer updated = customerRepository.save(customer);
        log.info("✅ Customer updated successfully");

        return updated;
    }

    // ✅ Validation Method for Create
    private void validateRequest(CustomerLoginRequest request) {
        Set<ConstraintViolation<CustomerLoginRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));

            throw new BusinessException(errorMessage);
        }
    }

    // ✅ Validation Method for Update (Password is optional)
    private void validateRequestForUpdate(CustomerLoginRequest request) {
        Set<ConstraintViolation<CustomerLoginRequest>> violations = validator.validate(request);

        // Filter out password validation if password is null/empty in update
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            violations = violations.stream()
                    .filter(v -> !v.getPropertyPath().toString().equals("password"))
                    .collect(Collectors.toSet());
        }

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));

            throw new BusinessException(errorMessage);
        }
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

    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Customer not found"));
    }

    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public void delete(Long id) {
        Customer customer = getById(id);

        if (customer.getImage() != null) {
            deleteImage(customer.getImage());
        }

        customerRepository.delete(customer);
    }

    private String generateVerifyCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
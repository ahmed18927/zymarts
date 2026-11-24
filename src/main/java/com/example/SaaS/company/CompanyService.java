//package com.example.SaaS.company;
//
//import com.example.SaaS.common.entity.Admin;
//import com.example.SaaS.common.entity.Company;
//import com.example.SaaS.common.exception.BusinessException;
//import com.example.SaaS.common.repository.AdminRepository;
//import com.example.SaaS.company.DTOS.CompanyCreateRequest;
//import com.example.SaaS.company.DTOS.CompanyResponse;
//import com.example.SaaS.company.DTOS.CompanyUpdateRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class CompanyService {
//
//    private final CompanyRepository companyRepository;
//    private final AdminRepository adminRepository;
//
//    public List<CompanyResponse> getAllCompanies(){
//        return companyRepository.findAll().stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    public CompanyResponse getCompanyById(Long id){
//        Company company = companyRepository.findById(id)
//                .orElseThrow(() -> new BusinessException("Company not found"));
//        return mapToResponse(company);
//    }
//
//    public CompanyResponse getCompanyByDomain(String domain) {
//        Company company = companyRepository.findByDomain(domain)
//                .orElseThrow(() -> new BusinessException("Company not found"));
//        return mapToResponse(company);
//    }
//
//    @Transactional
//    public CompanyResponse createCompany(CompanyCreateRequest request, String adminEmail){
//
//        if (companyRepository.findByDomain(request.getDomain()).isPresent()) {
//            throw new BusinessException("Domain already exists");
//        }
//
//        Admin admin = adminRepository.findByEmail(adminEmail)
//                .orElseThrow(() -> new BusinessException("Admin not found"));
//
//        Company company = Company.builder()
//                .name(request.getName())
//                .domain(request.getDomain())
//                .logo(request.getLogo())
//                .isActive(request.getIsActive() != null ? request.getIsActive() : false)
//                .addedBy(admin)
//                .createdAt(Instant.now())
//                .updatedAt(Instant.now())
//                .build();
//
//        company = companyRepository.save(company);
//        return mapToResponse(company);
//    }
//    @Transactional
//    public CompanyResponse updateCompanyByAdmin(Long id, CompanyUpdateRequest request) {
//        Company company = companyRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Company not found"));
//
//        updateCompanyFields(company, request);
//        company.setUpdatedAt(Instant.now());
//
//        company = companyRepository.save(company);
//        return mapToResponse(company);
//    }
//
//    @Transactional
//    public CompanyResponse updateCompanyByDomain(String domain, CompanyUpdateRequest request) {
//        Company company = companyRepository.findByDomain(domain)
//                .orElseThrow(() -> new RuntimeException("Company not found"));
//
//        updateCompanyFields(company, request);
//        company.setUpdatedAt(Instant.now());
//
//        company = companyRepository.save(company);
//        return mapToResponse(company);
//    }
//
//    @Transactional
//    public void deleteCompany(Long id) {
//        Company company = companyRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Company not found"));
//        companyRepository.delete(company);
//    }
//
//    @Transactional
//    public void deleteCompanyByDomain(String domain) {
//        Company company = companyRepository.findByDomain(domain)
//                .orElseThrow(() -> new RuntimeException("Company not found"));
//        companyRepository.delete(company);
//    }
//
//    @Transactional
//    public void activateCompany(Long id, boolean active) {
//        Company company = companyRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Company not found"));
//        company.setIsActive(active);
//        company.setUpdatedAt(Instant.now());
//        companyRepository.save(company);
//    }
//
//    private void updateCompanyFields(Company company, CompanyUpdateRequest request) {
//        if (request.getName() != null) {
//            company.setName(request.getName());
//        }
//        if (request.getDomain() != null) {
//            if (companyRepository.findByDomain(request.getDomain())
//                    .filter(c -> !c.getId().equals(company.getId()))
//                    .isPresent()) {
//                throw new RuntimeException("Domain already exists");
//            }
//            company.setDomain(request.getDomain());
//        }
//        if (request.getLogo() != null) {
//            company.setLogo(request.getLogo());
//        }
//    }
//
//    private CompanyResponse mapToResponse(Company company) {
//        return CompanyResponse.builder()
//                .id(company.getId())
//                .name(company.getName())
//                .domain(company.getDomain())
//                .logo(company.getLogo())
//                .isActive(company.getIsActive())
//                .addedBy(company.getAddedBy() != null ? company.getAddedBy().getName() : null)
//                .createdAt(company.getCreatedAt())
//                .updatedAt(company.getUpdatedAt())
//                .build();
//    }
//
//
//}
package com.example.SaaS.company;

import com.example.SaaS.common.entity.Admin;
import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.repository.AdminRepository;
import com.example.SaaS.company.DTOS.CompanyCreateRequest;
import com.example.SaaS.company.DTOS.CompanyResponse;
import com.example.SaaS.company.DTOS.CompanyUpdateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AdminRepository adminRepository;
    private final Validator validator;

    public Page<CompanyResponse> getAllCompanies(Pageable pageable) {
        log.info("📥 Getting all companies");
        return companyRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public CompanyResponse getCompanyById(Long id) {
        log.info("📥 Getting company by ID: {}", id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Company not found"));
        return mapToResponse(company);
    }

    public CompanyResponse getCompanyByDomain(String domain) {
        log.info("📥 Getting company by domain: {}", domain);
        Company company = companyRepository.findByDomain(domain)
                .orElseThrow(() -> new BusinessException("Company not found"));
        return mapToResponse(company);
    }

    @Transactional
    public CompanyResponse createCompany(CompanyCreateRequest request, String adminEmail) {

        // ✅ Validate Request
        validateCreateRequest(request);

        log.info("📥 Creating company: {} by admin: {}", request.getName(), adminEmail);

        if (companyRepository.findByDomain(request.getDomain()).isPresent()) {
            throw new BusinessException("Domain already exists");
        }

        Admin admin = adminRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new BusinessException("Admin not found"));

        Company company = Company.builder()
                .name(request.getName())
                .domain(request.getDomain())
                .logo(request.getLogo())
                .isActive(request.getIsActive() != null ? request.getIsActive() : false)
                .addedBy(admin)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        company = companyRepository.save(company);
        log.info("✅ Company created with ID: {}", company.getId());

        return mapToResponse(company);
    }

    @Transactional
    public CompanyResponse updateCompanyByAdmin(Long id, CompanyUpdateRequest request) {

        // ✅ Validate Request
        validateUpdateRequest(request);

        log.info("🔄 Updating company ID: {}", id);

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Company not found"));

        updateCompanyFields(company, request);
        company.setUpdatedAt(Instant.now());

        company = companyRepository.save(company);
        log.info("✅ Company updated successfully");

        return mapToResponse(company);
    }

    @Transactional
    public CompanyResponse updateCompanyByDomain(String domain, CompanyUpdateRequest request) {

        // ✅ Validate Request
        validateUpdateRequest(request);

        log.info("🔄 Updating company by domain: {}", domain);

        Company company = companyRepository.findByDomain(domain)
                .orElseThrow(() -> new BusinessException("Company not found"));

        updateCompanyFields(company, request);
        company.setUpdatedAt(Instant.now());

        company = companyRepository.save(company);
        log.info("✅ Company updated successfully");

        return mapToResponse(company);
    }

    @Transactional
    public void deleteCompany(Long id) {
        log.info("🗑️ Deleting company ID: {}", id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Company not found"));
        companyRepository.delete(company);
        log.info("✅ Company deleted");
    }

    @Transactional
    public void deleteCompanyByDomain(String domain) {
        log.info("🗑️ Deleting company by domain: {}", domain);
        Company company = companyRepository.findByDomain(domain)
                .orElseThrow(() -> new BusinessException("Company not found"));
        companyRepository.delete(company);
        log.info("✅ Company deleted");
    }

    @Transactional
    public void activateCompany(Long id, boolean active) {
        log.info("🔄 {} company ID: {}", active ? "Activating" : "Deactivating", id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Company not found"));
        company.setIsActive(active);
        company.setUpdatedAt(Instant.now());
        companyRepository.save(company);
        log.info("✅ Company {} successfully", active ? "activated" : "deactivated");
    }

    // ✅ Validation Methods
    private void validateCreateRequest(CompanyCreateRequest request) {
        Set<ConstraintViolation<CompanyCreateRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));

            log.error("❌ Validation failed: {}", errorMessage);
            throw new BusinessException(errorMessage);
        }
    }

    private void validateUpdateRequest(CompanyUpdateRequest request) {
        Set<ConstraintViolation<CompanyUpdateRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));

            log.error("❌ Validation failed: {}", errorMessage);
            throw new BusinessException(errorMessage);
        }
    }

    private void updateCompanyFields(Company company, CompanyUpdateRequest request) {
        if (request.getName() != null) {
            company.setName(request.getName());
        }
        if (request.getDomain() != null) {
            if (companyRepository.findByDomain(request.getDomain())
                    .filter(c -> !c.getId().equals(company.getId()))
                    .isPresent()) {
                throw new BusinessException("Domain already exists");
            }
            company.setDomain(request.getDomain());
        }
        if (request.getLogo() != null) {
            company.setLogo(request.getLogo());
        }
    }

    private CompanyResponse mapToResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .domain(company.getDomain())
                .logo(company.getLogo())
                .isActive(company.getIsActive())
                .addedBy(company.getAddedBy() != null ? company.getAddedBy().getName() : null)
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }
}
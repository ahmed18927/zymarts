package com.example.SaaS.superAdmin.company;


import com.example.SaaS.common.entity.Admin;
import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.repository.AdminRepository;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.DTOS.CompanyCreateRequest;
import com.example.SaaS.company.DTOS.CompanyResponse;
import com.example.SaaS.company.DTOS.CompanyUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminCompanyService {

    private final CompanyRepository companyRepository;
    private final AdminRepository adminRepository;

// =================== GET ALL ===================

    public Page<CompanyResponse> getAllCompanies(
            Pageable pageable
    ) {
        log.info("📥 Admin is getting all companies");

        return companyRepository
                .findAll(pageable)
                .map(this::mapToResponse);
    }

// =================== GET BY ID ===================

    public CompanyResponse getCompanyById(
            Long id
    ) {
        log.info(
                "📥 Admin is getting company with ID: {}",
                id
        );

        Company company = findCompanyById(id);

        return mapToResponse(company);
    }

// =================== CREATE ===================

    @Transactional
    public CompanyResponse createCompany(
            CompanyCreateRequest request,
            String adminEmail
    ) {
        log.info(
                "📥 Admin {} is creating company: {}",
                adminEmail,
                request.getName()
        );

        if (companyRepository
                .findByDomain(request.getDomain())
                .isPresent()) {

            throw new BusinessException(
                    "Domain already exists"
            );
        }

        Admin admin = adminRepository
                .findByEmail(adminEmail)
                .orElseThrow(
                        () -> new BusinessException(
                                "Admin not found"
                        )
                );

        Instant now = Instant.now();

        Company company = Company.builder()
                .name(request.getName())
                .domain(request.getDomain())
                .logo(request.getLogo())
                .isActive(
                        request.getIsActive() != null
                                ? request.getIsActive()
                                : true
                )
                .addedBy(admin)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Company savedCompany =
                companyRepository.save(company);

        log.info(
                "✅ Admin {} created company ID: {}",
                adminEmail,
                savedCompany.getId()
        );

        return mapToResponse(savedCompany);
    }

// =================== UPDATE ===================

    @Transactional
    public CompanyResponse updateCompany(
            Long id,
            CompanyUpdateRequest request
    ) {
        log.info(
                "🔄 Admin is updating company ID: {}",
                id
        );

        Company company = findCompanyById(id);

        if (request.getName() != null) {
            company.setName(
                    request.getName()
            );
        }

        if (request.getDomain() != null) {

            boolean domainExists =
                    companyRepository
                            .findByDomain(
                                    request.getDomain()
                            )
                            .filter(
                                    existingCompany ->
                                            !existingCompany
                                                    .getId()
                                                    .equals(
                                                            company.getId()
                                                    )
                            )
                            .isPresent();

            if (domainExists) {
                throw new BusinessException(
                        "Domain already exists"
                );
            }

            company.setDomain(
                    request.getDomain()
            );
        }

        if (request.getLogo() != null) {
            company.setLogo(
                    request.getLogo()
            );
        }

        company.setUpdatedAt(
                Instant.now()
        );

        Company updatedCompany =
                companyRepository.save(company);

        log.info(
                "✅ Company ID {} updated successfully",
                id
        );

        return mapToResponse(
                updatedCompany
        );
    }

// =================== ACTIVATE ===================

    @Transactional
    public CompanyResponse changeCompanyStatus(
            Long id,
            boolean active
    ) {
        log.info(
                "🔄 Admin is changing company ID {} status to {}",
                id,
                active
        );

        Company company = findCompanyById(id);

        company.setIsActive(active);
        company.setUpdatedAt(
                Instant.now()
        );

        Company updatedCompany =
                companyRepository.save(company);

        log.info(
                "✅ Company ID {} status updated",
                id
        );

        return mapToResponse(
                updatedCompany
        );
    }

// =================== DELETE ===================

    @Transactional
    public void deleteCompany(
            Long id
    ) {
        log.info(
                "🗑️ Admin is deleting company ID: {}",
                id
        );

        Company company = findCompanyById(id);

        companyRepository.delete(company);

        log.info(
                "✅ Company ID {} deleted successfully",
                id
        );
    }

// =================== PRIVATE METHODS ===================

    private Company findCompanyById(
            Long id
    ) {
        return companyRepository
                .findById(id)
                .orElseThrow(
                        () -> new BusinessException(
                                "Company not found"
                        )
                );
    }

    private CompanyResponse mapToResponse(
            Company company
    ) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .domain(company.getDomain())
                .logo(company.getLogo())
                .isActive(company.getIsActive())
                .addedBy(
                        company.getAddedBy() != null
                                ? company
                                .getAddedBy()
                                .getName()
                                : null
                )
                .createdAt(
                        company.getCreatedAt()
                )
                .updatedAt(
                        company.getUpdatedAt()
                )
                .build();
    }

}

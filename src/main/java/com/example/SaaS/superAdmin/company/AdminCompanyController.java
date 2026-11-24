package com.example.SaaS.superAdmin.company;

import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.company.DTOS.CompanyCreateRequest;
import com.example.SaaS.company.DTOS.CompanyResponse;
import com.example.SaaS.company.DTOS.CompanyUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/companies")
@RequiredArgsConstructor
@Slf4j
public class AdminCompanyController {

    private final AdminCompanyService adminCompanyService;

// =================== GET ALL ===================

    @GetMapping
    public ResponseEntity<
            ApiResponse<Page<CompanyResponse>>
            > getAllCompanies(

            @PageableDefault(
                    size = 10
            )
            Pageable pageable
    ) {

        Page<CompanyResponse> response =
                adminCompanyService
                        .getAllCompanies(
                                pageable
                        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Companies retrieved successfully",
                        response
                )
        );
    }

// =================== GET BY ID ===================

    @GetMapping("/{id}")
    public ResponseEntity<
            ApiResponse<CompanyResponse>
            > getCompanyById(

            @PathVariable Long id
    ) {

        CompanyResponse response =
                adminCompanyService
                        .getCompanyById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Company retrieved successfully",
                        response
                )
        );
    }

// =================== CREATE ===================

    @PostMapping
    public ResponseEntity<
            ApiResponse<CompanyResponse>
            > createCompany(

            @Valid
            @RequestBody
            CompanyCreateRequest request,

            Authentication authentication
    ) {

        String adminEmail =
                authentication.getName();

        CompanyResponse response =
                adminCompanyService
                        .createCompany(
                                request,
                                adminEmail
                        );

        return ResponseEntity
                .status(
                        HttpStatus.CREATED
                )
                .body(
                        new ApiResponse<>(
                                true,
                                "Company created successfully",
                                response
                        )
                );
    }

// =================== UPDATE ===================

    @PutMapping("/{id}")
    public ResponseEntity<
            ApiResponse<CompanyResponse>
            > updateCompany(

            @PathVariable Long id,

            @Valid
            @RequestBody
            CompanyUpdateRequest request
    ) {

        CompanyResponse response =
                adminCompanyService
                        .updateCompany(
                                id,
                                request
                        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Company updated successfully",
                        response
                )
        );
    }

// =================== CHANGE STATUS ===================

    @PatchMapping("/{id}/status")
    public ResponseEntity<
            ApiResponse<CompanyResponse>
            > changeCompanyStatus(

            @PathVariable Long id,

            @RequestParam boolean active
    ) {

        CompanyResponse response =
                adminCompanyService
                        .changeCompanyStatus(
                                id,
                                active
                        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        active
                                ? "Company activated successfully"
                                : "Company deactivated successfully",
                        response
                )
        );
    }

// =================== DELETE ===================

    @DeleteMapping("/{id}")
    public ResponseEntity<
            ApiResponse<Void>
            > deleteCompany(

            @PathVariable Long id
    ) {

        adminCompanyService
                .deleteCompany(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Company deleted successfully",
                        null
                )
        );
    }

}


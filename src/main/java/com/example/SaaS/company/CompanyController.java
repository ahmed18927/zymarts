//package com.example.SaaS.company;
//
//import com.example.SaaS.company.DTOS.CompanyCreateRequest;
//import com.example.SaaS.company.DTOS.CompanyResponse;
//import com.example.SaaS.company.DTOS.CompanyUpdateRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/companies")
//@RequiredArgsConstructor
//public class CompanyController {
//
//    private final CompanyService companyService;
//
//    // =================== CREATE ===================
//    @PostMapping
//    public CompanyResponse createCompany(
//            @RequestBody CompanyCreateRequest request,
//            @RequestHeader("Admin-Email") String adminEmail
//    ) {
//        return companyService.createCompany(request, adminEmail);
//    }
//
//    // =================== READ ===================
//    @GetMapping
//    public List<CompanyResponse> getAllCompanies() {
//        return companyService.getAllCompanies();
//    }
//
//    @GetMapping("/{id}")
//    public CompanyResponse getCompanyById(@PathVariable Long id) {
//        return companyService.getCompanyById(id);
//    }
//
//    @GetMapping("/by-domain/{domain}")
//    public CompanyResponse getCompanyByDomain(@PathVariable String domain) {
//        return companyService.getCompanyByDomain(domain);
//    }
//
//    // =================== UPDATE ===================
//    @PutMapping("/{id}")
//    public CompanyResponse updateCompanyByAdmin(
//            @PathVariable Long id,
//            @RequestBody CompanyUpdateRequest request
//    ) {
//        return companyService.updateCompanyByAdmin(id, request);
//    }
//
//    @PutMapping("/by-domain/{domain}")
//    public CompanyResponse updateCompanyByDomain(
//            @PathVariable String domain,
//            @RequestBody CompanyUpdateRequest request
//    ) {
//        return companyService.updateCompanyByDomain(domain, request);
//    }
//
//    // =================== DELETE ===================
//    @DeleteMapping("/{id}")
//    public void deleteCompany(@PathVariable Long id) {
//        companyService.deleteCompany(id);
//    }
//
//    @DeleteMapping("/by-domain/{domain}")
//    public void deleteCompanyByDomain(@PathVariable String domain) {
//        companyService.deleteCompanyByDomain(domain);
//    }
//
//    // =================== ACTIVATE / DEACTIVATE ===================
//    @PatchMapping("/{id}/activate")
//    public void activateCompany(
//            @PathVariable Long id,
//            @RequestParam boolean active
//    ) {
//        companyService.activateCompany(id, active);
//    }

package com.example.SaaS.company;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

    private final CompanyService companyService;

    // =================== CREATE ===================
    @PostMapping
    public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(
            @Valid @RequestBody CompanyCreateRequest request,
            @RequestHeader("Admin-Email") String adminEmail
    ) {
        log.info("📨 POST /api/companies - Creating company");
        CompanyResponse response = companyService.createCompany(request, adminEmail);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Company created successfully", response));
    }

    // =================== READ ===================
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CompanyResponse>>> getAllCompanies(
            @PageableDefault(size = 10) Pageable pageable) {

        log.info("📨 GET /api/companies - Getting all companies");
        Page<CompanyResponse> response = companyService.getAllCompanies(pageable);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Companies retrieved successfully", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompanyById(@PathVariable Long id) {
        log.info("📨 GET /api/companies/{}", id);
        CompanyResponse response = companyService.getCompanyById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company retrieved successfully", response)
        );
    }

    @GetMapping("/by-domain/{domain}")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompanyByDomain(@PathVariable String domain) {
        log.info("📨 GET /api/companies/by-domain/{}", domain);
        CompanyResponse response = companyService.getCompanyByDomain(domain);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company retrieved successfully", response)
        );
    }

    // =================== UPDATE ===================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponse>> updateCompanyByAdmin(
            @PathVariable Long id,
            @Valid @RequestBody CompanyUpdateRequest request
    ) {
        log.info("📨 PUT /api/companies/{}", id);
        CompanyResponse response = companyService.updateCompanyByAdmin(id, request);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company updated successfully", response)
        );
    }

    @PutMapping("/by-domain/{domain}")
    public ResponseEntity<ApiResponse<CompanyResponse>> updateCompanyByDomain(
            @PathVariable String domain,
            @Valid @RequestBody CompanyUpdateRequest request
    ) {
        log.info("📨 PUT /api/companies/by-domain/{}", domain);
        CompanyResponse response = companyService.updateCompanyByDomain(domain, request);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company updated successfully", response)
        );
    }

    // =================== DELETE ===================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable Long id) {
        log.info("📨 DELETE /api/companies/{}", id);
        companyService.deleteCompany(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company deleted successfully", null)
        );
    }

    @DeleteMapping("/by-domain/{domain}")
    public ResponseEntity<ApiResponse<Void>> deleteCompanyByDomain(@PathVariable String domain) {
        log.info("📨 DELETE /api/companies/by-domain/{}", domain);
        companyService.deleteCompanyByDomain(domain);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company deleted successfully", null)
        );
    }

    // =================== ACTIVATE / DEACTIVATE ===================
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateCompany(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {
        log.info("📨 PATCH /api/companies/{}/activate?active={}", id, active);
        companyService.activateCompany(id, active);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Company " + (active ? "activated" : "deactivated") + " successfully", null)
        );
    }
}

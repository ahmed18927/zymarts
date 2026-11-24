package com.example.SaaS.company;

import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.company.DTOS.CompanyRegistrationRequest;
import com.example.SaaS.company.DTOS.CompanyRegistrationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company/register")
@RequiredArgsConstructor
@Slf4j
public class CompanyRegistrationController {

    private final CompanyRegistrationService registrationService;

    /**
     * تسجيل شركة جديدة
     * POST /api/company/register
     *
     * يتطلب:
     * - بيانات الشركة (name, domain, logo)
     * - بيانات الموظف الرئيسي (name, email, phone, password, image)
     * - بيانات الاشتراك (packageId, durationMonths)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CompanyRegistrationResponse>> registerCompany(
            @Valid @ModelAttribute CompanyRegistrationRequest request
    ) {
        log.info("📨 POST /api/company/register - Registering new company: {}", request.getCompanyName());

        CompanyRegistrationResponse response = registrationService.registerCompany(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Company registered successfully! Please login with your credentials.",
                        response
                ));
    }
}

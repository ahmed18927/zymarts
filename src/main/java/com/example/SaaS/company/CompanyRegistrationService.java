package com.example.SaaS.company;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Employee;
import com.example.SaaS.common.entity.Package;
import com.example.SaaS.common.entity.Subscription;
import com.example.SaaS.common.exception.BusinessException;

import com.example.SaaS.common.service.FileStorageService;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.DTOS.CompanyRegistrationRequest;
import com.example.SaaS.company.DTOS.CompanyRegistrationResponse;
import com.example.SaaS.company.employee.EmployeeRepository;
import com.example.SaaS.company.subscription.SubscriptionRepository;
import com.example.SaaS.superAdmin.packagee.PackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyRegistrationService {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PackageRepository packageRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    /**
     * تسجيل شركة جديدة مع موظف رئيسي واشتراك
     */
    @Transactional
    public CompanyRegistrationResponse registerCompany(CompanyRegistrationRequest request) {

        log.info("🚀 Starting company registration for domain: {}", request.getDomain());

        // 1️⃣ التحقق من عدم تكرار البيانات
        validateUniqueConstraints(request);

        // 2️⃣ التحقق من وجود الباكدج
        Package selectedPackage = validateAndGetPackage(request.getPackageId());

        // 3️⃣ إنشاء الشركة
        Company company = createCompany(request);
        log.info("✅ Company created with ID: {}", company.getId());

        // 4️⃣ إنشاء الموظف الرئيسي
        Employee mainEmployee = createMainEmployee(request, company);
        log.info("✅ Main employee created with ID: {}", mainEmployee.getId());

        // 5️⃣ إنشاء الاشتراك
        Subscription subscription = createSubscription(company, selectedPackage, request.getDurationMonths());
        log.info("✅ Subscription created with ID: {}", subscription.getId());

        // 6️⃣ تفعيل الشركة
        company.setIsActive(true);
        companyRepository.save(company);

        log.info("🎉 Company registration completed successfully for: {}", company.getName());

        // 7️⃣ إرجاع الرد
        return buildResponse(company, mainEmployee, subscription, selectedPackage);
    }

    /**
     * التحقق من عدم تكرار Domain, Email, Phone
     */
    private void validateUniqueConstraints(CompanyRegistrationRequest request) {

        if (companyRepository.findByDomain(request.getDomain()).isPresent()) {
            throw new BusinessException("Domain already exists: " + request.getDomain());
        }

        if (employeeRepository.existsByEmail(request.getEmployeeEmail())) {
            throw new BusinessException("Email already exists: " + request.getEmployeeEmail());
        }

        if (employeeRepository.existsByPhone(request.getEmployeePhone())) {
            throw new BusinessException("Phone already exists: " + request.getEmployeePhone());
        }
    }

    /**
     * التحقق من وجود الباكدج وأنه نشط
     */
    private Package validateAndGetPackage(Long packageId) {
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new BusinessException("Package not found with ID: " + packageId));

        if (!pkg.getIsActive()) {
            throw new BusinessException("Package is not active");
        }

        return pkg;
    }

    /**
     * إنشاء الشركة
     */
    private Company createCompany(CompanyRegistrationRequest request) {
        String companyLogoPath = null;
        if (request.getCompanyLogo() != null && !request.getCompanyLogo().isEmpty()) {
            companyLogoPath = fileStorageService.saveFile(request.getCompanyLogo());
        }

        Company company = Company.builder()
                .name(request.getCompanyName())
                .domain(request.getDomain())
                .logo(companyLogoPath)
                .isActive(false) // هيتفعل بعد كدة
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return companyRepository.save(company);
    }

    /**
     * إنشاء الموظف الرئيسي (isMain = true)
     */
    private Employee createMainEmployee(CompanyRegistrationRequest request, Company company) {
        String employeeImagePath = null;
        if (request.getEmployeeImage() != null && !request.getEmployeeImage().isEmpty()) {
            employeeImagePath = fileStorageService.saveFile(request.getEmployeeImage());
        }

        Employee mainEmployee = Employee.builder()
                .name(request.getEmployeeName())
                .email(request.getEmployeeEmail())
                .phone(request.getEmployeePhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .image(employeeImagePath)
                .company(company)
                .isMain(true) // ⬅️ الموظف الرئيسي
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return employeeRepository.save(mainEmployee);
    }

    /**
     * إنشاء الاشتراك
     */
    private Subscription createSubscription(Company company, Package selectedPackage, Integer durationMonths) {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMonths(durationMonths);

        // تحويل بيانات الباكدج إلى JSON (أو أي طريقة تانية حسب نظامك)
        String packageData = buildPackageData(selectedPackage);

        Subscription subscription = Subscription.builder()
                .companyId(company)
                .packageId(selectedPackage)
                .startDate(startDate)
                .endDate(endDate)
                .status("ACTIVE")
                .packageData(packageData)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return subscriptionRepository.save(subscription);
    }

    /**
     * بناء بيانات الباكدج (JSON مثلاً)
     */
    private String buildPackageData(Package pkg) {
        return String.format(
                "{\"packageId\": %d, \"maxProducts\": %d, \"maxOrder\": %d, \"features\": \"%s\"}",
                pkg.getId(),
                pkg.getMaxProducts(),
                pkg.getMaxOrder(),
                pkg.getFeatures() != null ? pkg.getFeatures().replace("\"", "\\\"") : ""
        );
    }

    /**
     * بناء الرد
     */
    private CompanyRegistrationResponse buildResponse(
            Company company,
            Employee employee,
            Subscription subscription,
            Package selectedPackage
    ) {
        return CompanyRegistrationResponse.builder()
                .companyId(company.getId())
                .companyName(company.getName())
                .domain(company.getDomain())
                .companyLogo(company.getLogo())
                .isActive(company.getIsActive())
                .employeeId(employee.getId())
                .employeeName(employee.getName())
                .employeeEmail(employee.getEmail())
                .employeePhone(employee.getPhone())
                .employeeImage(employee.getImage())
                .isMainEmployee(employee.getIsMain())
                .subscriptionId(subscription.getId())
                .packageId(selectedPackage.getId())
                .packageName(getPackageName(selectedPackage))
                .subscriptionStartDate(subscription.getStartDate())
                .subscriptionEndDate(subscription.getEndDate())
                .subscriptionStatus(subscription.getStatus())
                .createdAt(Instant.now())
                .message("Company registered successfully! You can now login with your credentials.")
                .build();
    }

    /**
     * الحصول على اسم الباكدج من الترجمات أو قيمة افتراضية
     */
    private String getPackageName(Package pkg) {
        if (pkg.getTranslations() != null && !pkg.getTranslations().isEmpty()) {
            return pkg.getTranslations().get(0).getName();
        }
        return "Package #" + pkg.getId();
    }
}

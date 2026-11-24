package com.example.SaaS.company.theme;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Theme;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.security.EmployeeDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final CompanyRepository companyRepository;


    private Long getCurrentCompanyId() {
        return ((EmployeeDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getCompanyId();
    }

    public List<Theme> getAvailableThemes() {
        return themeRepository.findByIsActiveTrue();
    }

    public Company applyTheme(Long themeId) {
        Long companyId = getCurrentCompanyId();
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException("company not found"));

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new BusinessException("theme not found"));

        company.setThemeId(theme);
        return companyRepository.save(company);
    }

    public Company customizeTheme(ThemeCustomizeRequest request) {
        Long companyId = getCurrentCompanyId();
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException("company not found"));

        company.setCustomThemeSettings(request.getCustomThemeSettings());
        return companyRepository.save(company);
    }
}

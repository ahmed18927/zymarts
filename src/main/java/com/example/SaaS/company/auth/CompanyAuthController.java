package com.example.SaaS.company.auth;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.DTOS.CompanyLoginRequest;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
//@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyAuthController {

    private final CompanyRepository companyRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login (@RequestBody CompanyLoginRequest request){
        Company company = companyRepository.findByDomain(request.getDomain())
                .orElseThrow(() -> new BusinessException("Domain not found"));

        if (!company.getIsActive()){
            throw new BusinessException("Company is not active");
        }

        String token = jwtUtil.generateToken(company.getDomain() ,"COMPANY");
        return Map.of(
                "token", token,
                "companyId", company.getId().toString(),
                "name", company.getName(),
                "domain", company.getDomain()
        );
    }

    @PostMapping("/logout")
    public String logout(){
        return "success";
    }
}

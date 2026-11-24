package com.example.SaaS.superAdmin.auth;

import com.example.SaaS.common.entity.Admin;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {
    @Autowired
    private final SuperAdminRepository adminRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public SuperAdminResource login(AdminLoginRequest request) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Email not found"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new BusinessException("Invalid password");
        }

        String token = jwtUtil.generateToken(admin.getEmail(), "ADMIN");

        return SuperAdminResource.builder()
                .token(token)
                .id(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .build();
    }
}

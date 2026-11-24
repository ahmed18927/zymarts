package com.example.SaaS.superAdmin.auth;

import com.example.SaaS.common.entity.Admin;
import com.example.SaaS.common.enums.Role;
import com.example.SaaS.common.repository.AdminRepository;
import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private  final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody AdminRegisterRequest request){

        if (adminRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        Admin admin = Admin.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();

        adminRepository.save(admin);
        return "success";
    }


    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<SuperAdminResource>> login(@RequestBody AdminLoginRequest request) {
        SuperAdminResource resource = adminAuthService.login(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", resource));
    }
//@PostMapping("/login")
//public ResponseEntity<ApiResponse<SuperAdminResource>> login(@RequestBody AdminLoginRequest request){
//
//    Admin admin = adminRepository.findByEmail(request.getEmail())
//            .orElseThrow(() -> new RuntimeException("Email not found"));
//
//    if(!passwordEncoder.matches(request.getPassword(), admin.getPassword())){
//        throw new RuntimeException("Invalid password");
//    }
//
//    String token = jwtUtil.generateToken(admin.getEmail(), "ADMIN");
//    SuperAdminResource resource = SuperAdminResource.builder()
//            .token(token)
//            .id(admin.getId())
//            .name(admin.getName())
//            .email(admin.getEmail())
//            .build();
// return  ResponseEntity.ok(new ApiResponse<>(true ,"login successfully" ,resource ));
//    //    return Map.of("token", token);
//}

    @PostMapping("/logout")
    public String logout(){
        return "success";
    }
}

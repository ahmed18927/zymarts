package com.example.SaaS.company.auth;

import com.example.SaaS.common.entity.Employee;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.company.employee.EmployeeRepository;
import com.example.SaaS.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class AuthController {

    private final EmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final EmployeeAuthService employeeAuthService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<EmployeeResourceDTO>> login(@RequestBody EmployeeLoginRequest request) {
        EmployeeResourceDTO resource = employeeAuthService.login(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", resource));
    }
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody EmployeeLoginRequest request) {
//        String token = employeeAuthService.login(request.getEmail(), request.getPassword());
////        return ResponseEntity.ok(token);
//              return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", token));
//
//    }
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponse<EmployeeResourceDTO>> login(@RequestBody EmployeeLoginRequest request) {
//        // Find employee by email
//        Employee employee = employeeRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new BusinessException("Email not found"));
//
//        // Check password
//        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
//            throw new BusinessException("Invalid password");
//        }
//
//        // Generate JWT token
//        String token = jwtUtil.generateToken(employee.getEmail(), "COMPANY");
//
//        // Build EmployeeResource (like SuperAdminResource)
//        EmployeeResourceDTO resource = EmployeeResourceDTO.builder()
//                .token(token)
//                .id(employee.getId())
//                .name(employee.getName())
//                .email(employee.getEmail())
////                .companyId(employee.getCompany().getId())
////                .domain(employee.getCompany().getDomain())
//                .build();
//
//        return ResponseEntity.ok(new ApiResponse<>(true, "Login successfully", resource));
//    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Logout successfully", "success"));
    }
}

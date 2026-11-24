package com.example.SaaS.company.customer.AUTH;

import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.company.customer.DTOS.*;
import com.example.SaaS.security.GuestTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerAuthController {

    private final CustomerAuthService customerAuthService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @RequestBody CustomerRegisterRequest request, HttpServletRequest httpRequest) {

        String guestToken = httpRequest.getHeader(GuestTokenUtil.HEADER_NAME);

        customerAuthService.register(request, guestToken);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Registration successful. Check your email for the verification code.", null)
        );
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(
            @RequestBody VerifyEmailRequest request) {

        customerAuthService.verifyEmail(request);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Email verified successfully", null)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<CustomerResourceDTO>> login(
            @RequestBody CustomerLoginRequest request,
            HttpServletRequest httpRequest) {

        String guestToken = httpRequest.getHeader(GuestTokenUtil.HEADER_NAME);

        CustomerResourceDTO resource =
                customerAuthService.login(request, guestToken);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Login successful", resource)
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        customerAuthService.forgotPassword(request);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "If this email is registered, a reset code has been sent", null)
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        customerAuthService.resetPassword(request);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Password reset successfully", null)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Logout successfully", "success")
        );
    }
}

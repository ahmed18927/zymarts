package com.example.SaaS.company.customer.AUTH;

import com.example.SaaS.common.entity.Company;
import com.example.SaaS.common.entity.Customer;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.EmailService;
import com.example.SaaS.company.CompanyRepository;
import com.example.SaaS.company.cart.service.CartService;
import com.example.SaaS.company.customer.CustomerRepository;
import com.example.SaaS.company.customer.DTOS.*;
import com.example.SaaS.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CustomerAuthService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final CartService cartService;

    // ── Register ─────────────────────────────────────────────────────────────
    public void register(CustomerRegisterRequest request, String guestToken) {

        if (customerRepository.existsByEmail(request.getEmail()))
            throw new BusinessException("Email already in use");

        if (customerRepository.existsByPhone(request.getPhone()))
            throw new BusinessException("Phone already in use");

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new BusinessException("Company not found"));

        String code = generateCode();

        Customer customer = Customer.builder()
                .companyId(company)
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .verifyCode(code)
                .build();

        customerRepository.save(customer);

        if (guestToken != null && !guestToken.isBlank()) {
            cartService.mergeGuestCartIntoCustomer(guestToken, customer);
        }

        // comment this line out if you don't have mail configured yet
//        emailService.sendVerifyCode(request.getEmail(), request.getName(), code);
    }

    // ── Verify Email ─────────────────────────────────────────────────────────
    public void verifyEmail(VerifyEmailRequest request) {

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Customer not found"));

        if (customer.getVerifyEmail() != null)
            throw new BusinessException("Email already verified");

        if (!request.getCode().equals(customer.getVerifyCode()))
            throw new BusinessException("Invalid verification code");

        customer.setVerifyEmail(LocalDateTime.now());
        customer.setVerifyCode(null);
        customerRepository.save(customer);
    }

    // ── Login ─────────────────────────────────────────────────────────────────
    public CustomerResourceDTO login(CustomerLoginRequest request,
                                     String guestToken) {

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Email not found"));

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword()))
            throw new BusinessException("Invalid password");

        if (customer.getVerifyEmail() == null)
            throw new BusinessException("Please verify your email before logging in");

        if (guestToken != null && !guestToken.isBlank()) {
            cartService.mergeGuestCartIntoCustomer(guestToken, customer);
        }

        String token = jwtUtil.generateToken(customer.getEmail(), "CUSTOMER");

        return CustomerResourceDTO.builder()
                .token(token)
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .build();
    }

    // ── Forgot Password ───────────────────────────────────────────────────────
    public void forgotPassword(ForgotPasswordRequest request) {

        // silent fail — don't leak whether email exists
        customerRepository.findByEmail(request.getEmail()).ifPresent(customer -> {
            String code = generateCode();
            customer.setVerifyCode(code);
            customerRepository.save(customer);
//            emailService.sendResetCode(request.getEmail(), customer.getName(), code);
        });
    }

    // ── Reset Password ────────────────────────────────────────────────────────
    public void resetPassword(ResetPasswordRequest request) {

        if (!request.getNewPassword().equals(request.getConfirmPassword()))
            throw new BusinessException("Passwords do not match");

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Customer not found"));

        if (customer.getVerifyCode() == null
                || !request.getCode().equals(customer.getVerifyCode()))
            throw new BusinessException("Invalid or expired reset code");

        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customer.setVerifyCode(null);
        customerRepository.save(customer);
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private String generateCode() {
        return String.valueOf(100_000 + new Random().nextInt(900_000));
    }

}

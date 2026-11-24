package com.example.SaaS.payment.controller;

import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.payment.dto.PaymentResponse;
import com.example.SaaS.payment.service.PaymentService;
import com.example.SaaS.security.CustomerDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company/payments")
@RequiredArgsConstructor
public class CompanyPaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> getCompanyPayments(
            @AuthenticationPrincipal CustomerDetails userDetails,
            Pageable pageable
    ) {
        Long companyId = userDetails.getCompanyId();
        Page<PaymentResponse> payments = paymentService.getCompanyPayments(companyId, pageable);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Payments retrieved successfully",
                payments
        ));
    }

    @PostMapping("/{paymentId}/confirm")
    public ResponseEntity<ApiResponse<PaymentResponse>> confirmPayment(
            @AuthenticationPrincipal CustomerDetails userDetails,
            @PathVariable Long paymentId
    ) {
        Long companyId = userDetails.getCompanyId();
        PaymentResponse response = paymentService.confirmPayment(paymentId, companyId);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Payment confirmed successfully",
                response
        ));
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<ApiResponse<PaymentResponse>> refundPayment(
            @AuthenticationPrincipal CustomerDetails userDetails,
            @PathVariable Long paymentId,
            @RequestParam(required = false) String reason
    ) {
        Long companyId = userDetails.getCompanyId();
        PaymentResponse response = paymentService.refundPayment(paymentId, companyId, reason);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Payment refunded successfully",
                response
        ));
    }
}

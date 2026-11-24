package com.example.SaaS.payment.controller;

import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.payment.dto.PaymentRequest;
import com.example.SaaS.payment.dto.PaymentResponse;
import com.example.SaaS.payment.service.PaymentService;
import com.example.SaaS.security.CustomerDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/payments")
@RequiredArgsConstructor
@Slf4j
public class CustomerPaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @AuthenticationPrincipal CustomerDetails userDetails,
            @Valid @RequestBody PaymentRequest request
    ) {
        Long customerId = userDetails.getCustomerId();
        Long companyId = userDetails.getCompanyId();

        PaymentResponse response = paymentService.createStripePayment(customerId, companyId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Payment initiated successfully",
                        response
                ));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @AuthenticationPrincipal CustomerDetails userDetails,
            @PathVariable Long paymentId
    ) {
        Long customerId = userDetails.getCustomerId();
        PaymentResponse response = paymentService.getPayment(paymentId, customerId);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Payment retrieved successfully",
                response
        ));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByOrder(
            @AuthenticationPrincipal CustomerDetails userDetails,
            @PathVariable Long orderId
    ) {
        Long customerId = userDetails.getCustomerId();
        PaymentResponse response = paymentService.getPaymentByOrder(orderId, customerId);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Payment retrieved successfully",
                response
        ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> getMyPayments(
            @AuthenticationPrincipal CustomerDetails userDetails,
            Pageable pageable
    ) {
        Long customerId = userDetails.getCustomerId();
        Page<PaymentResponse> payments = paymentService.getCustomerPayments(customerId, pageable);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Payments retrieved successfully",
                payments
        ));
    }
}

package com.example.SaaS.payment.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String status;
    private String gatewayPaymentId;
    private String gatewayTransactionId;
    private String errorMessage;
    private Instant paidAt;
    private Instant createdAt;
    private Instant updatedAt;

    // For frontend redirect URLs
    private String checkoutUrl;
    private String clientSecret; // For Stripe frontend
}

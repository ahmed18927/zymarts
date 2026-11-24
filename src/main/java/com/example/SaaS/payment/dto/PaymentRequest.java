package com.example.SaaS.payment.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @NotNull(message = "Order ID is required")
    @Positive(message = "Order ID must be positive")
    private Long orderId;

    @NotBlank(message = "Payment method is required")
    @Pattern(
        regexp = "STRIPE|PAYMOB|PAYPAL|CASH|WALLET",
        message = "Payment method must be STRIPE, PAYMOB, PAYPAL, CASH, or WALLET"
    )
    private String paymentMethod;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Size(max = 3, min = 3, message = "Currency must be 3 characters")
    private String currency = "USD";

    // For Stripe
    private String stripePaymentMethodId;
    private String stripeToken;

    // For Paymob
    private String paymobToken;

    // For PayPal
    private String paypalOrderId;
}

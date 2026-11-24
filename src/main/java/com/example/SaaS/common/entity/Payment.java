package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @NotNull(message = "Order is required")
    private Order orderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer is required")
    private Customer customerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull(message = "Company is required")
    private Company companyId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(max = 3, min = 3, message = "Currency must be 3 characters (ISO 4217)")
    @Column(nullable = false, length = 3)
    private String currency = "USD";

    @NotBlank(message = "Payment method is required")
    @Pattern(
        regexp = "STRIPE|PAYMOB|PAYPAL|CASH|WALLET",
        message = "Payment method must be STRIPE, PAYMOB, PAYPAL, CASH, or WALLET"
    )
    @Column(nullable = false, length = 20, name = "payment_method")
    private String paymentMethod;

    @NotBlank(message = "Status is required")
    @Pattern(
        regexp = "PENDING|PROCESSING|SUCCESS|FAILED|REFUNDED|CANCELLED",
        message = "Status must be PENDING, PROCESSING, SUCCESS, FAILED, REFUNDED, or CANCELLED"
    )
    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    // Stripe/Paymob/PayPal IDs
    @Size(max = 255)
    @Column(name = "gateway_payment_id")
    private String gatewayPaymentId;

    @Size(max = 255)
    @Column(name = "gateway_transaction_id")
    private String gatewayTransactionId;

    @Column(name = "gateway_response", columnDefinition = "TEXT")
    private String gatewayResponse;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "refunded_at")
    private Instant refundedAt;

    @DecimalMin(value = "0.00")
    @Digits(integer = 10, fraction = 2)
    @Column(precision = 12, scale = 2, name = "refund_amount")
    private BigDecimal refundAmount;

    @Size(max = 500)
    @Column(name = "refund_reason")
    private String refundReason;

    // Metadata JSON
    @Column(columnDefinition = "TEXT")
    private String metadata;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = "PENDING";
        }
        if (this.currency == null) {
            this.currency = "USD";
        }
    }
}

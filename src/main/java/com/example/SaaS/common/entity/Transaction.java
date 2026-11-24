package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @NotNull(message = "Wallet is required")
    @Positive(message = "Wallet id must be positive")
    @JoinColumn(name = "wallet_id")
    private Wallet walletId;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "subscription_id")
    @Positive(message = "Subscription id must be positive")
    private Subscription subscription;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.00", message = "Amount must be greater than 0")
//    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false, name = "amount")
    private BigDecimal amount;

    @NotBlank(message = "Type is required")
    @Pattern(
            regexp = "^(credit|debit|REFUND)$",
            message = "Type must be credit , debit or REFUND")

    @NotNull(message = "Balance before is required")
    @DecimalMin(value = "0.01", message = "Balance before must not be negative")
    @Digits(integer = 12, fraction = 2)
    @Column(nullable = false, precision = 14, scale = 2, name = "balance_before")
    private BigDecimal balanceBefore;

    @NotNull(message = "Balance after is required")
    @DecimalMin(value = "0.00", message = "Balance after must not be negative")
    @Digits(integer = 12, fraction = 2)
    @Column(nullable = false, precision = 14, scale = 2, name = "balance_after")
    private BigDecimal balanceAfter;

    @ManyToOne(fetch = FetchType.LAZY,optional = true)
    @Positive(message = "Order ID must be positive")
    @JoinColumn(name = "order_id")
    private Order orderId;

    @Size(max = 2000, message = "Payment data must not exceed 2000 characters")
    @Column(columnDefinition = "TEXT", name = "payment_data")
    private String paymentData;

    @Pattern(
            regexp = "PENDING|SUCCESS|FAILED",
            message = "Payment status must be PENDING, SUCCESS, or FAILED"
    )
    @Column(length = 20, name = "payment_status")
    private String paymentStatus;

    @CreationTimestamp
    @PastOrPresent(message = "Created date must be in the past or present")
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @PastOrPresent(message = "Updated date must be in the past or present")
    @Column(name = "updated_at")
    private Instant updatedAt;

}

package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Table(
        name = "wallets",
        uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "customer_id"})
)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "company_id")
    @NotNull(message = "Company id is required")
    @Positive(message = "Company id must be positive")
    private Company companyId;

    @Column(nullable = false, name = "balance")
    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.00", message = "Balance must be greater than 0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "customer_id")
    @NotNull(message = "Customer id is required")
    @Positive(message = "Customer id must be positive")
    private Customer customerId;

    @PrePersist
    public void prePersist() {
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
    }
}

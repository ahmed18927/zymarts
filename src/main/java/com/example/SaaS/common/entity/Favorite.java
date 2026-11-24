package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
        name = "favorites",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"customer_id", "product_id"})
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Favorite {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @NotNull(message = "Product is required")
    @JoinColumn(nullable = false, name = "product_id")
    private Product productId;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @NotNull(message = "Customer is required")
    @JoinColumn(nullable = false,name = "customer_id")
    private Customer customerId;
}

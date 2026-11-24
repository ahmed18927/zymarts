package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Table(name = "addresses")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "customer_id")
    @NotNull(message = "Customer id is required")
    private Customer customerId;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    @Column(nullable = false, name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @NotNull(message = "City id is required")
    @JoinColumn(nullable = false, name = "city_id")
    private City cityId;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @NotNull(message = "Country id is required")
    @JoinColumn(nullable = false, name = "country_id")
    private Country countryId;

    @Column(name = "is_default")
    @NotNull(message = "Is default is required")
    private Boolean isDefault;

    @NotNull(message = "Active flag is required")
    @Column(name = "is_active")
    private Boolean isActive;

    @Size(max = 500, message = "Note must be less than 255 characters")
    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "addressId")
    private List<Order> orders;

}

package com.example.SaaS.common.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "company_id")
    @NotNull(message = "Company id is required")
    private Company companyId;

    @Column(nullable = false, name = "name")
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Column(nullable = false, name = "phone", unique = true)
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone must be 10 to 15  digits")
    private String phone;

    @Column(nullable = false, name = "email", unique = true)
    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false, name = "password")
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100 ,message = "Password must be between 6 and 100 characters")
    private String password;

    @Column( name = "image")
    private String image;

    @Column(nullable = true, name = "verify_email")
    @PastOrPresent(message = "Verify email must be in the present")
    private LocalDateTime verifyEmail ;

    @Column(nullable = true, name = "verify_phone")
    @PastOrPresent(message = "Verify phone timestamp must be in the past or present")
    private LocalDateTime verifyPhone;

    @Column(nullable = false, name = "verify_code")
    @NotBlank(message = "Verify code is required")
    @Size(min = 4, max = 8, message = "Verify code must be 4 to 8 characters")
    private String verifyCode;

    @OneToMany(
            mappedBy = "customerId",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "customerId")
    private List<Cart> carts;

    @OneToMany(mappedBy = "customerId")
    private List<Order> orders;

    @OneToMany(mappedBy = "customerId")
    private List<Rate> rates;

}

package com.example.SaaS.common.entity;
import com.example.SaaS.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    private Instant verifyEmail;
    private Instant verifyPhone;

    private String password;

    private String verifyCode;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "is_main")
    private Boolean isMain = false;

//    @OneToMany(mappedBy = "addedBy", cascade = CascadeType.ALL)
//    private List<Category> categories;

    @Enumerated(EnumType.STRING)
    private Role role = Role.EMPLOYEE;

    public Long getCompanyId() {
        return company != null ? company.getId() : null;
    }

    @OneToMany(mappedBy = "addedBy", cascade = CascadeType.ALL)
    private java.util.List<SubCategory> subCategoriesAdded;


    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}
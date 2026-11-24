package com.example.SaaS.common.entity;

import com.example.SaaS.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name ="admins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;
     private String name;
     private String password;
     @Column(unique = true)
     private String email;
     @Column(unique = true)
     private String phone;
     private Instant verifyEmail;
     private Instant verifyPhone;
     private String verifyCode;
     private String image;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ADMIN;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}

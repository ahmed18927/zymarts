package com.example.SaaS.superAdmin.auth;

import lombok.Data;

@Data
public class AdminLoginRequest {
    private String email;
    private String password;
}

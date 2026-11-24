package com.example.SaaS.superAdmin.auth;

import lombok.Data;

@Data
public class AdminRegisterRequest {
    private String email;
    private String password;
    private String name;
}

package com.example.SaaS.company.auth;

import lombok.Data;

@Data
public class EmployeeLoginRequest {
    private String email;
    private String password;
}

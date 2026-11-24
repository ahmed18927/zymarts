package com.example.SaaS.superAdmin.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuperAdminResource {
    private String token;

    private Long id;
    private String name;
    private String email;
}

package com.example.SaaS.superAdmin.auth;

import com.example.SaaS.common.entity.Admin;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class SuperAdminDetailsService  implements UserDetailsService {

    private final SuperAdminRepository repository;

    public SuperAdminDetailsService(SuperAdminRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Admin admin = repository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Super Admin not found"));

        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .roles("SUPER_ADMIN")
                .build();
    }
}

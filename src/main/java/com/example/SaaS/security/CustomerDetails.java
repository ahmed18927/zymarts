package com.example.SaaS.security;

import com.example.SaaS.common.entity.Customer;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomerDetails implements UserDetails {

//    private final Customer customer;
//
//    public CustomerDetails(Customer customer) {
//        this.customer = customer;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("CUSTOMER"));
//    }
//
//    @Override public String getPassword() { return customer.getPassword(); }
//    @Override public String getUsername() { return customer.getEmail();    }
//    @Override public boolean isAccountNonExpired()     { return true; }
//    @Override public boolean isAccountNonLocked()      { return true; }
//    @Override public boolean isCredentialsNonExpired() { return true; }
//    @Override public boolean isEnabled() {
//        return customer.getVerifyEmail() != null;
//    }
//}
private final Customer customer;

    public CustomerDetails(Customer customer) {
        this.customer = customer;
    }

    public Long getCustomerId() {
        return customer.getId();
    }

    public Long getCompanyId() {
        return customer.getCompanyId().getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("CUSTOMER"));
    }

    @Override public String getPassword() { return customer.getPassword(); }
    @Override public String getUsername() { return customer.getEmail(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return customer.getVerifyEmail() != null; }
}
package com.example.SaaS.security;

import com.example.SaaS.common.entity.Employee;
import com.example.SaaS.company.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Employee employee = employeeRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("Employee not found: " + email));
//
//        // Create UserDetails with authority = "EMPLOYEE" (table-based)
//        return new org.springframework.security.core.userdetails.User(
//                employee.getEmail(),
//                employee.getPassword(),
//                List.of(new SimpleGrantedAuthority("EMPLOYEE")) // authority = EMPLOYEE
//        );
//    }
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found: " + email));

        return new EmployeeDetails(employee); // return custom UserDetails
    }

}

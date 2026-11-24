package com.example.SaaS.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil                jwtUtil;
    private final AdminDetailsService    adminDetailsService;
    private final EmployeeDetailsService employeeDetailsService;
    private final CustomerDetailsService customerDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/admin/login")              ||
                path.startsWith("/api/admin/register")           ||
                path.startsWith("/api/company/login")            ||
                path.startsWith("/api/company/register")         ||
                path.startsWith("/api/customer/register")        ||
                path.startsWith("/api/customer/login")           ||
                path.startsWith("/api/customer/verify-email")    ||
                path.startsWith("/api/customer/forgot-password") ||
                path.startsWith("/api/customer/reset-password") ||
                path.startsWith("/api/public/**");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                if (!jwtUtil.isTokenValid(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                String email = jwtUtil.extractEmail(token);
                String type  = jwtUtil.extractType(token);

                UserDetails userDetails = switch (type) {
                    case "ADMIN"    -> adminDetailsService.loadUserByUsername(email);
                    case "EMPLOYEE" -> employeeDetailsService.loadUserByUsername(email);
                    case "CUSTOMER" -> customerDetailsService.loadUserByUsername(email);
                    default -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        yield null;
                    }
                };

                if (userDetails == null) return;

                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
package com.example.SaaS.config;

import com.example.SaaS.security.AdminDetailsService;
import com.example.SaaS.security.EmployeeDetailsService;
import com.example.SaaS.security.GuestTokenFilter;
import com.example.SaaS.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AdminDetailsService adminDetailsService;
    private final EmployeeDetailsService employeeDetailsService; // NEW
    private final GuestTokenFilter guestTokenFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // ✅ Public endpoints
                        .requestMatchers(
                                "/api/admin/login",
                                "/api/admin/register",
                                "/api/company/login",
                                "/api/company/register",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/customer/register",
                                "/api/customer/login",
                                "/api/customer/verify-email",
                                "/api/customer/forgot-password",
                                "/api/customer/reset-password"
                        ).permitAll()

                        .requestMatchers("/api/customer/**")
                        .hasAuthority("CUSTOMER")
                        // ✅ Admin endpoints
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")

                        .requestMatchers("/api/cart/**").permitAll()

                        // ✅ Employee endpoints
                        .requestMatchers("/api/company/**").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/landPage/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        // any other request needs authentication
                        .anyRequest().authenticated()
                )
                // Add JWT filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .addFilterAfter(guestTokenFilter, JwtAuthFilter.class)
                // Stateless session (JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://192.168.1.8:5173"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
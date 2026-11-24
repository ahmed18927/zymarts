package com.example.SaaS.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GuestTokenFilter extends OncePerRequestFilter {

    public static final String REQUEST_ATTR_GUEST_TOKEN = "guestToken";

    private final GuestTokenUtil guestTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        boolean authenticated = SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if (!authenticated) {
            String guestToken = request.getHeader(GuestTokenUtil.HEADER_NAME);
            if (guestToken == null || guestToken.isBlank()) {
                guestToken = guestTokenUtil.generate();
            }
            request.setAttribute(REQUEST_ATTR_GUEST_TOKEN, guestToken);
            response.setHeader(GuestTokenUtil.HEADER_NAME, guestToken);
        }

        filterChain.doFilter(request, response);
    }
}

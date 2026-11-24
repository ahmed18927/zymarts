package com.example.SaaS.company.cart.util;

import com.example.SaaS.security.CustomerDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CartContextResolver {

    /** Returns the authenticated customer's email, or null if the caller is a guest. */
    public String resolveAuthenticatedEmail() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomerDetails customerDetails) {
            return customerDetails.getUsername();
        }
        return null;
    }

    /** Returns the guest token set by GuestTokenFilter, or null if the caller is authenticated. */
    public String resolveGuestToken(HttpServletRequest request) {
        Object attr = request.getAttribute("guestToken");
        return attr == null ? null : attr.toString();
    }
}
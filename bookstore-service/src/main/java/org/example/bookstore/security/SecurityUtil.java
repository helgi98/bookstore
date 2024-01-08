package org.example.bookstore.security;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@UtilityClass
public class SecurityUtil {

    public String getCurrentUserId() {
        return getCurrentUserJwt().getClaimAsString("userId");
    }

    private Jwt getCurrentUserJwt() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Authentication is missing");
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }

        throw new IllegalStateException("Authentication is not Jwt");
    }

}

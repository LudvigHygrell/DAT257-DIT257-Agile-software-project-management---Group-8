package com.backend.jwt.user;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

public abstract class UserUtil {
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                return authentication.getName();
            } else {
                return null;
            }
        } else {
            return null;
            
        }
    }
}

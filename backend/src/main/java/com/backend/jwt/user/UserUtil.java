package com.backend.jwt.user;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

/**
 * Utilities for getting information about authenticated users.
 */
public abstract class UserUtil {

    /**
     * Get the the username of the user associated with the authentication in this Rest instance.
     * <p>
     * Note: This is the same as the user that sent the request (if they were authenticated).
     */
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        //
        // Note to devs: Previous implementation returned null. This is usually very bad.
        // NullPointerExceptions are the root of all evil, either make the intent of maybe
        // returning nothing clear by returning value of type Optional or throw an exception.
        //
        throw new RuntimeException("Failed to get named user authentication.");
    }
}

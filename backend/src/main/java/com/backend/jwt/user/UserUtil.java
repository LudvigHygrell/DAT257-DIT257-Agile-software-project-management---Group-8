package com.backend.jwt.user;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

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

    /**
     * Checks whether the current request included a valid Authorization header.
     */
    public static boolean isAuthenticated() {

        RequestAttributes  attrs = RequestContextHolder.getRequestAttributes();
        if (!(attrs instanceof ServletRequestAttributes)) {
            return false;
        }
        HttpServletRequest req = ((ServletRequestAttributes)attrs).getRequest();
        String auth = req.getHeader("Authorization");
        return auth != null && auth.startsWith("Bearer ");
    }
}

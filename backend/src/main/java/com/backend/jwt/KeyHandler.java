package com.backend.jwt;

import java.security.Key;

import org.springframework.stereotype.Component;

@Component
public class KeyHandler {
    private static final String SECRET = "";
    
    private Key get_key() {
        return null;
    }

    public String generate_token(String username) {
        return null;
    }

    public String extract_username(String token) {
        return null;        
    }

    public boolean is_valid_token(String token) {
        return true;
    }

    private boolean is_expired(String token) {
        return false;
    }

}

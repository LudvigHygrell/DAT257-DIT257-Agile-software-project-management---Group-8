package com.backend.database;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Hashing utilities for hashing and comparing passwords.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-18
 */
@Service
public class PasswordHashUtility {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Hash a password for save DB insertion.
     *
     * @param password Password to hash
     * @return The hashed version of the password.
     */
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Compares a literal password to a password hash that was stored in the DB.
     *
     * @param hashedPassword Hashed version of the password to decrypt.
     * @param password Password to compare to.
     * @return True if the password matched the hash.
     */
    public boolean passwordMatches(String hashedPassword, String password) {
        return hashPassword(password).equals(hashedPassword);
    }
}

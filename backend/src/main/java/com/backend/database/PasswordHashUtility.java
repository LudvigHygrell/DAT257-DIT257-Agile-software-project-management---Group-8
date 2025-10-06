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
     * Class used to differentiate between a literal password string and a password "digest"/hash.
     */
    public static final class Digest implements Comparable<Digest> {
        private final String contents;

        /**
         * Manually create a digest object from the literal digest string.
         * @param contents The digest string (not password).
         */
        public Digest(String contents) {
            this.contents = contents;
        }

        /**
         * Gets the digest as a string.
         */
        @Override
        public String toString() {
            return contents;
        }

        @Override
        public boolean equals(Object object) {
            return object instanceof Digest && ((Digest)object).contents.equals(contents);
        }

        @Override
        public int compareTo(Digest other) {
            return contents.compareTo(other.contents);
        }

        @Override
        public int hashCode() {
            return contents.hashCode();
        }
    }

    /**
     * Hash a password for save DB insertion.
     *
     * @param password Password to hash
     * @return The hashed version of the password.
     */
    public Digest hashPassword(String password) {
        return new Digest(passwordEncoder.encode(password));
    }

    /**
     * Compares a literal password to a password hash that was stored in the DB.
     *
     * @param hashedPassword Hashed version of the password to decrypt.
     * @param password Password to compare to.
     * @return True if the password matched the hash.
     */
    public boolean passwordMatches(Digest hashedPassword, String password) {
        return passwordEncoder.matches(password, hashedPassword.toString());
    }
}

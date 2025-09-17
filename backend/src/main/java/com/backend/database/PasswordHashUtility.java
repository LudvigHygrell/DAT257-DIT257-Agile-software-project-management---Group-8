package com.backend.database;

/**
 * Hashing utilities for hashing and comparing passwords.
 */
public abstract class PasswordHashUtility {

    /**
     * Hash a password for save DB insertion.
     *
     * @param password Password to hash
     * @return The hashed version of the password.
     */
    public static String hashPassword(String password) {
        return password; // TODO
    }

    /**
     * Compares a literal password to a password hash that was stored in the DB.
     *
     * @param hashedPassword Hashed version of the password to decrypt.
     * @param password Password to compare to.
     * @return True if the password matched the hash.
     */
    public static boolean passwordMatches(String hashedPassword, String password) {
        return hashPassword(password).equals(hashedPassword);
    }
}

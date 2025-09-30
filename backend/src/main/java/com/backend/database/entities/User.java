package com.backend.database.entities;

import com.backend.database.PasswordHashUtility;
import jakarta.persistence.*;

/**
 * Represents a record in the Users table.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-18
 */
@Entity
@Table(name="users")
public class User {

    public static final String DELETED_USER_NAME = "<deleted user>";
    public static final String GOD_ADMIN_USER_NAME = "<super>";

    @Id
    @Column(name="username", nullable=false)
    private String userName;

    @Column(name="email", nullable=false)
    private String email;

    @Column(name="userpassword", nullable=false)
    private String passwordHash;

    protected User() {
        userName = "";
        email = "";
        passwordHash = "";
    }

    /**
     * Constructs a new User record.
     *
     * @param uname Name of the user.
     * @param email Email of the user.
     * @param pwHash Hash of the user's password.
     */
    public User(String uname, String email, PasswordHashUtility.Digest pwHash) {
        assert null != uname;
        assert null != email;
        assert null != pwHash;
        this.userName = uname;
        this.email = email;
        this.passwordHash = pwHash.toString();
    }

    @Override
    public String toString() {
        return String.format("User(userName=%s, email=%s, password=%s)", userName, email, passwordHash);
    }

    public void setUserName(String name) {
        assert null != name;
        userName = name;
    }

    public void setEmail(String email) {
        assert null != email;
        this.email = email;
    }

    public void setPasswordHash(PasswordHashUtility.Digest hash) {
        passwordHash = hash.toString();
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public PasswordHashUtility.Digest getPasswordHash() {
        return new PasswordHashUtility.Digest(passwordHash);
    }
}

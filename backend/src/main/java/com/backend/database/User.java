package com.backend.database;

import jakarta.persistence.*;

/**
 * Represents a record in the Users table.
 */
@Entity
public class User {

    public static final String DELETED_USER_NAME = "<deleted user>";
    public static final String GOD_ADMIN_USER_NAME = "<super>";

    @Id
    @Column(name="username", nullable=false)
    private String userName;

    @Column(name="email", nullable=false)
    private String email;

    @Column(name="password", nullable=false)
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
    public User(String uname, String email, String pwHash) {
        assert null != uname;
        assert null != email;
        assert null != pwHash;
        this.userName = uname;
        this.email = email;
        this.passwordHash = pwHash;
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

    public void setPasswordHash(String hash) {
        passwordHash = hash;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}

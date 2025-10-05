package com.backend.database.adapters;

import com.backend.database.repositories.*;
import com.backend.database.entities.*;
import com.backend.database.*;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Restricted view into the Users table.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-18
 */
@Service
public class UserAdapter {

    @Autowired
    private PasswordHashUtility passwordHasher;

    @Autowired
    private final UserRepository userRepository;

    /**
     * Construct a new adapter.
     * @param repo Repository of users to view.
     */
    public UserAdapter(UserRepository repo) {
        userRepository = repo;
    }

    /**
     * Performs a login check given the provided credentials.
     *
     * @param username Name of the user to log in.
     * @param password The literal password of the user.
     * @return true if the password hash matched the stored hash for the given user's password.
     */
    public boolean login(String username, String password) {
        PasswordHashUtility.Digest passwordHash = userRepository.getReferenceById(username).getPasswordHash();
        return passwordHasher.passwordMatches(passwordHash, password);
    }

    /**
     * Checks if the given user already exists.
     *
     * @param name Name of the user.
     * @return true if the user exists.
     */
    public boolean isUsername(String name) {
        return userRepository.existsById(name);
    }

    /**
     * True if email exists in the db.
     */
    public boolean isEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Adds a new user to the db.
     * @param username Name of the user.
     * @param email Email of the user.
     * @param password Password string (not digest) of the user.
     */
    @Transactional
    public void register(String username, String email, String password) {

        if (username.contains("@"))
            throw new RuntimeException("Username cannot contain the '@' character.");

        userRepository.saveAndFlush(
                new User(username, email, passwordHasher.hashPassword(password)));
    }

    /**
     * Deletes a user from the db.
     * @param user Username of the user to delete.
     */
    public void deleteUser(String user) {
        userRepository.deleteById(user);
    }

    /**
     * Changes the password of a user in the db.
     * @param username Name of the user.
     * @param newPassword Password string (not digest) of the password to set.
     */
    public void changePassword(String username, String newPassword) {
        Optional<User> user = userRepository.findById(username);
        if (user.isEmpty())
            throw new RuntimeException(String.format("%s does not exist.", username));
        user.get().setPasswordHash(passwordHasher.hashPassword(newPassword));
        userRepository.save(user.get());
    }

    /**
     * Gets the user associated with the specified email address.
     * @param email Email of the user.
     * @return The user with the specified email, or Optional.empty()
     */
    public Optional<String> getUsernameFromEmail(String email) {
        return userRepository.findByEmail(email).map(User::getEmail);
    }

    /**
     * Edit the email of a user in the db.
     * @param username Username of the user to edit the email of.
     * @param email New email of the user.
     */
    public void changeEmail(String username, String email) {
        User user = userRepository.getReferenceById(username);
        user.setEmail(email);
        userRepository.saveAndFlush(user);
    }

    /**
     * Gets the stored password digest of a specified user.
     * @param username Username of the user to get the password digest of.
     * @return The digest (or hash) of the password, or Optional.empty().
     */
    public Optional<PasswordHashUtility.Digest> getPassword(String username) {
        return userRepository.findById(username).map(User::getPasswordHash);
    }
}

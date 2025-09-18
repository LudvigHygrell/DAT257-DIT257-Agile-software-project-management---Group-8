package com.backend.database;

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
public class UserAdapter extends Adapter {

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
        return PasswordHashUtility.passwordMatches(
                userRepository.getReferenceById(username).getPasswordHash(), password);
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

    public boolean isEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void register(String username, String email, String password) {
        userRepository.saveAndFlush(
                new User(username, email, PasswordHashUtility.hashPassword(password)));
    }

    public void deleteUser(String user) {
        userRepository.deleteById(user);
    }

    public void changePassword(String username, String newPassword) {
        Optional<User> user = userRepository.findById(username);
        if (user.isEmpty())
            throw new RuntimeException(String.format("%s does not exist.", username));
        user.get().setPasswordHash(PasswordHashUtility.hashPassword(newPassword));
        userRepository.save(user.get());
    }
}

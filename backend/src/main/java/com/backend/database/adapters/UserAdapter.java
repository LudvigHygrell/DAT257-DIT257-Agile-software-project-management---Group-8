package com.backend.database.adapters;

import com.backend.database.PasswordHashUtility;
import com.backend.database.entities.User;
import com.backend.database.repositories.UserRepository;
import com.backend.filesystem.PrivateFilesystem;

import jakarta.persistence.EntityManager;
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

    private final UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AuthorityInfo authInfo;

    @Autowired
    private PrivateFilesystem userFilesystem;

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
        PasswordHashUtility.Digest passwordHash;
        try {
            passwordHash = userRepository.getReferenceById(username).getPasswordHash();
        } catch (Exception ex) {
            return false;
        }
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
     * This method manually updates all related records to reference '<deleted>' user
     * before deleting the user to avoid foreign key constraint violations.
     * @param user Username of the user to delete.
     */
    @Transactional
    public void deleteUser(String user) {

        // Proceed only if this is the current user, or if a higher admin
        //
        if (!authInfo.hasModifyPermissionOnUser(user))
            throw new RuntimeException("Permission denied.");

        // Ensure the <deleted> placeholder user exists
        // This user is required for foreign key references
        entityManager.createNativeQuery(
            "INSERT INTO users (username, email, userpassword) " +
            "VALUES ('<deleted>', 'benesphere@blackhole.mx', '2bh1jkb34334') " +
            "ON CONFLICT (username) DO NOTHING")
            .executeUpdate();

        // Update all tables that reference this user to point to '<deleted>' instead
        // This is necessary because ON DELETE SET DEFAULT doesn't work in PostgreSQL
        // when the row being deleted is the one triggering the constraint check

        // Update searchedcharities
        entityManager.createNativeQuery(
            "UPDATE searchedcharities SET username = '<deleted>' WHERE username = :username")
            .setParameter("username", user)
            .executeUpdate();

        // Update charityscores (votes)
        entityManager.createNativeQuery(
            "UPDATE charityscores SET ratinguser = '<deleted>' WHERE ratinguser = :username")
            .setParameter("username", user)
            .executeUpdate();

        // Update comments
        entityManager.createNativeQuery(
            "UPDATE comments SET commentuser = '<deleted>' WHERE commentuser = :username")
            .setParameter("username", user)
            .executeUpdate();

        // Update commentscores
        entityManager.createNativeQuery(
            "UPDATE commentscores SET scoreuser = '<deleted>' WHERE scoreuser = :username")
            .setParameter("username", user)
            .executeUpdate();

        // Update commentblame
        entityManager.createNativeQuery(
            "UPDATE commentblame SET reporter = '<deleted>' WHERE reporter = :username")
            .setParameter("username", user)
            .executeUpdate();

        // Update charityblame
        entityManager.createNativeQuery(
            "UPDATE charityblame SET reporter = '<deleted>' WHERE reporter = :username")
            .setParameter("username", user)
            .executeUpdate();

        // Flush changes to ensure they're committed before deletion
        entityManager.flush();

        // Now delete the user - all foreign key references have been updated
        userRepository.deleteById(user);

        // Delete the user's local filesystem
        userFilesystem.scram();
    }

    /**
     * Changes the password of a user in the db.
     * @param username Name of the user.
     * @param newPassword Password string (not digest) of the password to set.
     */
    @Transactional
    public void changePassword(String username, String newPassword) {

        if (!authInfo.hasModifyPermissionOnUser(username))
            throw new RuntimeException("Permission denied.");

        Optional<User> user = userRepository.findById(username);
        if (user.isEmpty())
            throw new RuntimeException(String.format("%s does not exist.", username));
        user.get().setPasswordHash(passwordHasher.hashPassword(newPassword));
        userRepository.save(user.get());
    }

    /**
     * Gets the username associated with the specified email address.
     * @param email Email of the user.
     * @return The username with the specified email, or Optional.empty()
     */
    public Optional<String> getUsernameFromEmail(String email) {
        return userRepository.findByEmail(email).map(User::getUserName);
    }

    /**
     * Edit the email of a user in the db.
     * @param username Username of the user to edit the email of.
     * @param email New email of the user.
     */
    @Transactional
    public void changeEmail(String username, String email) {

        if (!authInfo.hasModifyPermissionOnUser(username))
            throw new RuntimeException("Permission denied.");

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

package com.backend.email;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

/**
 * Singleton that holds email addresses yet to be confirmed.
 * @author JaarmaCo
 * @since 2025-10-05
 * @version 1.0
 */
@Service
public final class EmailConfirmations {

    private static volatile EmailConfirmations instance = null;

    private final Random random = new Random();
    private final Map<String, Long> pending = new LinkedHashMap<>();
    private final Map<String, CompletableFuture<String>> pendingTasks = new LinkedHashMap<>();

    private EmailConfirmations() {}

    /**
     * Gets the singleton instance.
     */
    public static EmailConfirmations getInstance() {
        if (null == instance)
            instance = new EmailConfirmations();
        return instance;
    }

    /**
     * Adds a pending email confirmation.
     * @param email Email to confirm.
     * @return The confirmation code.
     */
    public synchronized long addPending(String email) {

        if (pending.containsKey(email))
            throw new RuntimeException("Email already pending.");

        CompletableFuture<String> task = new CompletableFuture<>();
        pending.put(email, random.nextLong(0, 999));
        pendingTasks.put(email, task);
        return pending.get(email);
    }

    /**
     * Gets a pending confirmation.
     * @param email Email that is awaiting confirmation.
     * @return A future that completes to the email address when the email is confirmed.
     */
    public synchronized CompletableFuture<String> getPending(String email) {

        if (!pendingTasks.containsKey(email))
            throw new RuntimeException("Email confirmation was not pending.");

        return pendingTasks.get(email);
    }

    /**
     * Gets the confirmation code of a pending confirmation.
     * @param email Email that is awaiting confirmation.
     * @return The confirmation code of the pending confirmation.
     */
    public synchronized long getCodeOfPending(String email) {
        if (!pending.containsKey(email))
            throw new RuntimeException("Email confirmation was not pending.");
        return pending.get(email);
    }

    /**
     * True if the given email is pending confirmation.
     */
    public synchronized boolean isPending(String email) {
        return pending.containsKey(email);
    }

    /**
     * Cancels an active email confirmation.
     * @param email Email to cancel the confirmation of.
     * @return True if the confirmation was cancelled.
     */
    public synchronized boolean abort(String email) {

        if (!pendingTasks.containsKey(email))
            return false;
        
        pendingTasks.get(email).completeExceptionally(new RuntimeException("Confirmation was aborted."));

        pending.remove(email);
        pendingTasks.remove(email);
        return true;
    }

    /**
     * Confirm an email address.
     * @param email Email address to confirm.
     * @param code Confirmation code of the email address.
     * @return True if confirmed.
     */
    public synchronized boolean confirm(String email, long code) {
        try {
            if (!pending.containsKey(email) || !pending.containsValue(code))
                return false;

            pending.remove(email);
            pendingTasks.get(email).complete(email);
            pendingTasks.remove(email);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

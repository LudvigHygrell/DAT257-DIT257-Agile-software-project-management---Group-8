package com.backend.database.adapters;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.ApplicationProperties;
import com.backend.database.entities.EmailConfirmation;
import com.backend.database.repositories.EmailConfirmationRepository;
import com.backend.interfaces.ThrowingCallback;

import jakarta.transaction.Transactional;

@Service
public class EmailConfirmationAdapter {
    
    @Autowired
    private ApplicationProperties props;

    @Autowired
    private EmailConfirmationRepository confRepo;

    /**
     * Consume an active email confirmation and return it's status, or return false.
     * 
     * @param email Email to test the verified status of.
     * @return true if the email was verified and wasn't expired.
     * @return false if email verification is disabled, the email wasn't pending, 
     * or the verification code expired or was not verified.
     */
    @Transactional
    public boolean isVerified(String email) {
        try {
            if (!props.getEmailProperties().isVerified())
                return true;

            Optional<EmailConfirmation> conf = confRepo.findById(email);
            if (conf.isEmpty())
                return false;

            if (conf.get().expired() || !conf.get().getConfirmed())
                return false;

            confRepo.delete(conf.get());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Add an email to the list of pending email confirmations, and then atomically an operation. Should the following
     * operation fail, the verification is removed.
     * 
     * @param email Email to verify.
     * @param next Operation to apply atomically once the email verification is pending.
     * @throws Exception Thrown if adding the confirmation failed, or if the following operation failed.
     */
    @Transactional
    public void addPendingVerification(String email, ThrowingCallback<EmailConfirmation> next) throws Exception {

        EmailConfirmation conf = new EmailConfirmation(email);
        conf = confRepo.save(conf);
        try {
            next.perform(conf);
        } catch (Exception ex) {
            confRepo.delete(conf);
            throw ex;
        }
    }

    /**
     * Signal that the provided pending email confirmation is verified.
     * 
     * @param email Email being confirmed.
     * @param uuid Token associated with the confirmation.
     * @throws Exception Thrown if the email confirmation was not pending, or if the confirmation token expired.
     */
    @Transactional
    public void verify(String email, UUID uuid) throws Exception {
        
        Optional<EmailConfirmation> conf = confRepo.findById(email);

        if (conf.isEmpty())
            throw new Exception("Confirmation did not exist.");

        if (!conf.get().getConfirmCode().equals(uuid))
            throw new Exception("Tokens did not match.");

        if (conf.get().expired()) {
            confRepo.delete(conf.get());
            throw new Exception("Token has expired.");
        }

        conf.get().setConfirmed(true);
        confRepo.save(conf.get());
    }
}

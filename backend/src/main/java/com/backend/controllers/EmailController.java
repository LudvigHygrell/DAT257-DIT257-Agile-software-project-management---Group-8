package com.backend.controllers;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.ApplicationProperties;
import com.backend.database.entities.EmailConfirmation;
import com.backend.database.repositories.EmailConfirmationRepository;
import com.backend.email.EmailService;

/**
 * Controller for requests sent from email links.
 * @author JaarmaCo
 * @since 2025-10-05
 * @version 1.0
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private ApplicationProperties props;

    @Autowired
    private EmailConfirmationRepository confRepo;

    @Autowired
    private EmailService service;

    private final Map<String, CompletableFuture<Void>> pendingOperations = new HashMap<>();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleServerError(Exception ex) {
        System.err.println(ex.getMessage());
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping("/confirm/{email}")
    public ResponseEntity<String> requestConfirm(@PathVariable(value="email", required=true) String email) throws Exception {

        if (!props.getEmailProperties().isVerified())
            return ResponseEntity.badRequest().body("Email confirmations are disabled.");

        if (!EmailValidator.getInstance().isValid(email))
            return ResponseEntity.badRequest().body("Malformed email.");

        try {
            EmailConfirmation conf = new EmailConfirmation(email);
            conf = confRepo.save(conf);

            service.sendEmailConfirmation(conf.getEmail(), conf.getConfirmCode());
        
            synchronized (pendingOperations) {
                pendingOperations.put(email, new CompletableFuture<>());
            }
            return ResponseEntity.ok().body("Confirmation link sent to: " + email);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                .body("Failed to send link, please try again.");
        }
    }

    @GetMapping("/await/{email}")
    public CompletableFuture<ResponseEntity<String>> waitForConfirmation(@PathVariable(value="email", required=true) String email) throws Exception {

        CompletableFuture<Void> fut;
        synchronized (pendingOperations) {
            if (!pendingOperations.containsKey(email)) {
                return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().body("No pending confirmation for email: " + email));
            }
            fut = pendingOperations.get(email);
        }
        return fut.thenApply(x -> ResponseEntity.ok().body("Email confirmed: " + email))
            .exceptionally(t -> ResponseEntity.internalServerError().body("Failed to confirm email."));
    }

    /**
     * Accepts email confirmation links.null
     * @param email Email address (encoded in URL-B64) to confirm.  
     * @param confirmCode Confirmation code.
     * @return A status message.
     */
    @GetMapping("/confirm/{email}/{confirmCode}")
    public ResponseEntity<Void> confirm(
        @PathVariable(value="email", required=true) String email,
        @PathVariable(value="confirmCode", required=true) UUID confirmCode) throws Exception {
        
        try {
            if (!props.getEmailProperties().isVerified())
                return ResponseEntity.badRequest().build();

            if (!EmailValidator.getInstance().isValid(email))
                throw new Exception();
            
            Optional<EmailConfirmation> conf = confRepo.findById(email);

            if (conf.isEmpty())
                throw new Exception();

            if (conf.get().expired()) {
                confRepo.delete(conf.get());
                throw new Exception();
            }

            conf.get().setConfirmed(true);
            confRepo.save(conf.get());

            synchronized (pendingOperations) {
                if (pendingOperations.containsKey(email)) {
                    pendingOperations.get(email).complete(null);
                    pendingOperations.remove(email);
                }
            }

            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:5173/email-confirm?status=success"))
                .build();
        } catch (Exception ex) {
            synchronized (pendingOperations) {
                if (pendingOperations.containsKey(email)) {
                    pendingOperations.get(email).completeExceptionally(ex);
                    pendingOperations.remove(email);
                }
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:5173/email-confirm?status=failure"))
                    .build();
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> needsConfirm() {
        return props.getEmailProperties().isVerified() ? ResponseEntity.ok().body("yes") 
            : ResponseEntity.noContent().build();
    }
}

package com.backend.controllers;

import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.email.EmailConfirmations;

/**
 * Controller for requests sent from email links.
 * @author JaarmaCo
 * @since 2025-10-05
 * @version 1.0
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {
    
    /**
     * Accepts email confirmation links.
     * @param email Email address (encoded in URL-B64) to confirm.  
     * @param confirmCode Confirmation code.
     * @return A status message.
     */
    @PostMapping("/confirm/{email}/{confirmCode}")
    public ResponseEntity<String> confirm(
        @PathVariable(value="email", required=true) String email,
        @PathVariable(value="confirmCode", required=true) Long confirmCode) {

        if (null == email)
            return ResponseEntity.status(400).body("Expected a confirmation email.");

        if (null == confirmCode)
            return ResponseEntity.status(400).body("Expected an email confirmation code.");
        try {
            email = new String(Base64.getUrlDecoder()
                .decode(email.getBytes()));
        } catch (Exception ex) {
            return ResponseEntity.status(400).body("Expected B64 formatted email.");
        }
        if (!EmailConfirmations.getInstance()
                .confirm(email, confirmCode)) {
            return ResponseEntity.status(500).body("Failed to confirm email.");
        }
        return ResponseEntity.ok().body("Email confirmed");
    }
}

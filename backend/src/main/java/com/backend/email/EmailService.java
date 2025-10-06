package com.backend.email;

import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.config.EmailProperties;

import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

/**
 * Service for sending emails.
 * @author jaarmaCo
 * @since 2025-10-05
 * @version 1.0
 */
@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender sender;

    private final EmailProperties emailProperties;

    private String getConfirmationEmailText(String email, long confirmCode) {

        final String HTML_TEMPLATE = """
        <!doctype HTML>
        <html>
            <head>
                <title>Confirm email</title>
            </head>
            <body>
                <p>
                    <a href="%s/email/confirm/%s/%s">Click to confirm email address.</a>
                </p>
            </body>
        </html>""";

        return String.format(HTML_TEMPLATE,
            ServletUriComponentsBuilder.fromCurrentContextPath()
                .build().toUriString(),
            new String(Base64.getUrlEncoder().encode(email.getBytes())),
            Long.toString(confirmCode));
    }

    public EmailService(EmailProperties properties) {
        emailProperties = properties;
    }

    /**
     * Validates that the given string is an email.
     * @param email Email to check.
     * @return True if email was an email.
     */
    public boolean validateEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    /**
     * Sends a confirmation email to a client's email address.
     * @param email Email address to send to.
     * @return A future that evaluates to the confirmed email address when it completes.
     */
    public CompletableFuture<String> sendEmailConfirmation(String email) throws Exception {
   
        EmailConfirmations confirmations = EmailConfirmations.getInstance();

        MimeMessage message = sender.createMimeMessage();
        message.setSubject("Benesphere email confirmation");

        String messageBody = getConfirmationEmailText(email, confirmations.addPending(email));

        MimeBodyPart body = new MimeBodyPart();
        body.setText(messageBody, "UTF-8", "html");
        
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(body);

        message.setContent(mp);

        message.setFrom(emailProperties.getUsername());
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

        sender.send(message);
        return confirmations.getPending(email);
    }
}

package com.backend.tests.controllers;

import java.io.InputStream;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

@TestConfiguration
public class MockUserLoginTestConfig {

    private final JavaMailSender mailSender = new JavaMailSender() {

        @Override
        public void send(@NonNull SimpleMailMessage... simpleMessages) throws MailException {
            return;
        }

        @Override
        public @NonNull MimeMessage createMimeMessage() {
            return new MimeMessage((Session)null);
        }

        @Override
        public @NonNull MimeMessage createMimeMessage(@NonNull InputStream contentStream) throws MailException {
            return new MimeMessage((Session)null);
        }

        @Override
        public void send(@NonNull MimeMessage... mimeMessages) throws MailException {
            return;
        }
        
    };

    @Bean
    public JavaMailSender javaMailSender() {
        return mailSender;
    }
}

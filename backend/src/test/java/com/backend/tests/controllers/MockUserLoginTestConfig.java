package com.backend.tests.controllers;

import java.io.InputStream;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

@TestConfiguration
public class MockUserLoginTestConfig {

    private final JavaMailSender mailSender = new JavaMailSender() {

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {
            return;
        }

        @Override
        public MimeMessage createMimeMessage() {
            return new MimeMessage((Session)null);
        }

        @Override
        public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
            return new MimeMessage((Session)null);
        }

        @Override
        public void send(MimeMessage... mimeMessages) throws MailException {
            return;
        }
        
    };

    @Bean
    public JavaMailSender javaMailSender() {
        return mailSender;
    }
}

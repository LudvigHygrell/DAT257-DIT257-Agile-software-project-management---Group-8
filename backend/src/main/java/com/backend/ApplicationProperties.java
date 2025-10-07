package com.backend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.backend.config.EmailProperties;

@Configuration
@ConfigurationProperties(prefix="benesphere")
public class ApplicationProperties {

    private boolean debug = false;

    private final EmailProperties email;

    public ApplicationProperties(EmailProperties emailProps) {
        email = emailProps;
    }

    public EmailProperties getEmailProperties() {
        return email;
    }

    public boolean inDebug() {
        return debug;
    }

    public boolean inRelease() {
        return !debug;
    }
}

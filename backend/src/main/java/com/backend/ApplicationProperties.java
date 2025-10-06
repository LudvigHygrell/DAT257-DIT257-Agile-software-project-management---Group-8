package com.backend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.backend.config.EmailProperties;

@Configuration
@ConfigurationProperties(prefix="benesphere")
public class ApplicationProperties {

    private final EmailProperties email;

    public ApplicationProperties(EmailProperties emailProps) {
        email = emailProps;
    }

    public EmailProperties getEmailProperties() {
        return email;
    }
}

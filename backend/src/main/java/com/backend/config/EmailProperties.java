package com.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="benesphere.email")
public class EmailProperties {

    private String username = "benesphere";
    private boolean verify;

    public boolean isVerified() {
        return verify;
    }

    public String getUsername() {
        return username;
    }
}
package com.backend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="benesphere")
public class ApplicationProperties {

    private boolean debug = false;

    private EmailProperties email;

    public static class EmailProperties {

        private String username = "benesphere";
        private boolean verify;

        public boolean isVerified() {
            return verify;
        }

        public void setVerify(boolean verify) {
            this.verify = verify;
        }

        public boolean getVerify() {
            return verify;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public void setEmail(EmailProperties properties) {
        this.email = properties;
    }

    public EmailProperties getEmail() {
        return email;
    }

    public EmailProperties getEmailProperties() {
        return email;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean getDebug() {
        return debug;
    }

    public boolean inDebug() {
        return debug;
    }

    public boolean inRelease() {
        return !debug;
    }
}

package com.backend.database.entities;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="pendingemailconfirmations")
public class EmailConfirmation {
    
    @Id
    @Column(name="email")
    private String email;

    @Column(name="confirmed")
    private boolean confirmed;

    @Column(name="confirmcode", columnDefinition="uuid")
    private UUID confirmCode;

    @Column(name="expiresat", columnDefinition="timestamp")
    private Timestamp expiresAt;

    protected EmailConfirmation() {}

    public EmailConfirmation(String email) {
        this.email = email;
        this.confirmed = false;
        this.confirmCode = UUID.randomUUID();
        this.expiresAt = Timestamp.from(Instant.now().plus(2, ChronoUnit.HOURS));
    }

    public UUID getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(UUID confirmCode) {
        this.confirmCode = confirmCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean expired() {
        return expiresAt != null && expiresAt.toInstant().isBefore(Instant.now());
    }
}

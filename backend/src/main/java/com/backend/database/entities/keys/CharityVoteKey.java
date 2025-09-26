package com.backend.database.entities.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;

/**
 * Composite primary key type for the Like table
 * @author Ludvighygrell
 * @version 1.0
 * @since 2025-09-18
 */
@Embeddable
public class CharityVoteKey implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;

    @Column(name="user")
    private String user;

    @Column(name="charity")
    private String charity;

    /**
     * Create a new Like key
     * @param user the user who is voting
     * @param charity the charity that is being voted on
     */
    public CharityVoteKey(String user, String charity) {
        assert null != user;
        assert null != charity;
        this.user = user;
        this.charity = charity;
    }

    public String getUser() {
        return user;
    }

    public String getCharity() {
        return charity;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    public void setCharity(String charity) {
        this.charity = charity;
    }
}

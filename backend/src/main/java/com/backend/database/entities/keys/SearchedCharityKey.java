package com.backend.database.entities.keys;

import java.io.Serial;
import java.io.Serializable;

/**
 * Id class for SearchedCharity entities.
 */
public class SearchedCharityKey implements Serializable {
    
    @Serial
    public static final long serialVersionUID = 1; 

    private String username;
    private String charity;

    protected SearchedCharityKey() {}

    /**
     * Create a new searched charity key.
     * @param username Name of the user that searched for the charity.
     * @param charity Charity that was searched for.
     */
    public SearchedCharityKey(String username, String charity) {
        assert null != username;
        assert null != charity;
        this.username = username;
        this.charity = charity;
    }

    /**
     * Gets the name of the user part of the key.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the charity part of the key.
     */
    public String getCharity() {
        return charity;
    }
}

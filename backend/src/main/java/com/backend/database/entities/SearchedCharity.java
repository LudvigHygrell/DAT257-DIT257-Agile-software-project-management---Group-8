package com.backend.database.entities;

import java.sql.Timestamp;

import com.backend.database.entities.keys.SearchedCharityKey;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entity for the SearchCharities table used to store user search history.
 */
@Entity
@IdClass(SearchedCharityKey.class)
@Table(name="searchedcharities")
public class SearchedCharity {
    
    @Id
    @Column(name="username")
    private String username;
    
    @Id
    @Column(name="charity")
    private String charity;

    @Column(name="visited")
    private boolean visited;

    @Column(name="inserttime", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp insertTime;

    protected SearchedCharity() {}

    /**
     * Create a new search entry.
     * @param username Name of the user.
     * @param charity Charity that was searched for.
     */
    public SearchedCharity(String username, String charity) {
        assert null != username;
        assert null != charity;

        this.username = username;
        this.charity = charity;
        this.visited = false;
    }

    /**
     * Create a new search entry.
     * @param username Name of the user.
     * @param charity Charity that was searched for.
     * @param visited Whether the charity page was explicitly opened by the user.
     */
    public SearchedCharity(String username, String charity, boolean visited) {
        assert null != username;
        assert null != charity;

        this.username = username;
        this.charity = charity;
        this.visited = visited;
    }

    /**
     * Get the username of the user that searched for the searched charity.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the searched charity organization id.
     */
    public String getCharity() {
        return charity;
    }

    /**
     * Get the time of the search.
     */
    public Timestamp getInsertTime() {
        return insertTime;
    }

    /**
     * Gets whether this search entry was explicitly openend by the user.
     */
    public boolean getVisited() {
        return visited;
    }

    /**
     * Set the username of the search entry.
     */
    public void setUsername(String username) {
        assert null != username;
        this.username = username;
    }

    /**
     * Set the charity of the search entry.
     */
    public void setCharity(String charity) {
        assert null != charity;
        this.charity = charity;
    }

    /**
     * Sets whether this search entry was explicitly opened by the user.
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Convert a searched charity entry to it's json representation.
     */
    public JsonNode toJson() {
        return JsonNodeFactory.instance.objectNode()
            .put("username", username)
            .put("charity", charity)
            .put("visited", visited)
            .put("insertTime", insertTime.getTime());
    }
}

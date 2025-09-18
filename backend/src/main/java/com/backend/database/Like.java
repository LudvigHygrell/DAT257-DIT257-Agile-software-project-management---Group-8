package com.backend.database;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Represents a record in the Likes table.
 * @author LudvigHygrell
 * @version 1.0
 * @since 2025-09-18
 */
@Entity
@Table(name="Likes")
public class Like {
    @EmbeddedId
    private LikeKey key;
    @Column(name = "insertTime")
    private LocalDateTime insertTime;

    protected Like() {}


    /**
     * Create a new Like object.
     * @param user The user who is voting.
     * @param charity The charity that is being voted on.
     * @param insertTime The time tha vote was made.
     */
    public Like(String user, String charity, LocalDateTime insertTime) {
        assert null != user;
        assert null != charity;
        assert null != insertTime;
        this.key = new LikeKey(user, charity);
        this.insertTime = insertTime;
    }
    public String getUser(){
        return key.getUser();
    }
    public void setUser(String user){
        key.setUser(user);
    }
    public String getCharity() {
        return key.getCharity();
    }
    public void setCharity(String charity) {
        key.setCharity(charity);
    }
    public LocalDateTime getInsertTime() {
        return insertTime;
    }
    public void setInsertTime(LocalDateTime insertTime) {
        this.insertTime = insertTime;
    }
}

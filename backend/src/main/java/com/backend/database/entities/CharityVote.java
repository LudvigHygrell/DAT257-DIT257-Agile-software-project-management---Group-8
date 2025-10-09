package com.backend.database.entities;

import java.sql.Timestamp;

import com.backend.database.entities.keys.*;

import jakarta.persistence.*;

/**
 * Represents a record in the Likes table.
 * @author LudvigHygrell
 * @version 1.0
 * @since 2025-09-18
 */
@Entity
@IdClass(CharityVoteKey.class)
@Table(name="charityscores")
public class CharityVote {
    
    @Id
    @Column(name="ratinguser")
    private String user;

    @Id
    @Column(name="charity")
    private String charity;

    @Column(name="vote")
    private boolean vote;

    @Column(name="inserttime", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp insertTime;

    protected CharityVote() {}


    /**
     * Create a new Like object.
     * @param user The user who is voting.
     * @param charity The charity that is being voted on.
     * @param vote Whether the user voted for or against the charity.
     */
    public CharityVote(String user, String charity, boolean vote) {
        assert null != user;
        assert null != charity;
        this.user = user;
        this.charity = charity;
        this.vote = vote;
    }
    
    public String getUser(){
        return user;
    }
    
    public void setUser(String user){
        assert null != user;
        this.user = user;
    }
    
    public String getCharity() {
        return charity;
    }
    
    public void setCharity(String charity) {
        assert null != charity;
        this.charity = charity;
    }
    
    public boolean votedUp() {
        return vote;
    }

    public void setVotedUp(boolean value) {
        vote = value;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }
}

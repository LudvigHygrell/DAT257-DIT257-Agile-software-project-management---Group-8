package com.backend.database.entities;

import com.backend.database.entities.keys.*;

import jakarta.persistence.*;

/**
 * Represents a record in the Likes table.
 * @author LudvigHygrell
 * @version 1.0
 * @since 2025-09-18
 */
@Entity
@Table(name="charityscores")
public class CharityVote {
    
    @EmbeddedId
    private CharityVoteKey key;

    @Column(name="vote")
    private boolean vote;

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
        this.key = new CharityVoteKey(user, charity);
        this.vote = vote;
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
    
    public boolean votedUp() {
        return vote;
    }

    public void setVotedUp(boolean value) {
        vote = value;
    }
}

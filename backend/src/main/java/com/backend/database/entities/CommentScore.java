package com.backend.database.entities;

import com.backend.database.entities.keys.CommentBlameKey;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name="commentscores")
@IdClass(CommentBlameKey.class)
public class CommentScore {
    
    @Id
    @Column(name="scoreuser")
    private String reporter;

    @Id
    @Column(name="comment")
    private int commentId;

    @Id
    @Column(name="charity")
    private String charity;

    @Column(name="updown")
    private boolean upDown;

    protected CommentScore() {}

    public CommentScore(String user, String charity, int commentId, boolean upDown) {
        this.reporter = user;
        this.charity = charity;
        this.upDown = upDown;
        this.commentId = commentId;
    }

    public String getReporter() {
        return reporter;
    }

    public String getCharity() {
        return charity;
    }

    public int getCommentId() {
        return commentId;
    }

    public boolean getUpDown() {
        return upDown;
    }

    public void setReporter(String user) {
        this.reporter = user;
    }

    public void setCharity(String charity) {
        this.charity = charity;
    }

    public void setCommentId(int id) {
        commentId = id;
    }

    public void setUpDown(boolean upDown) {
        this.upDown = upDown;
    }
}

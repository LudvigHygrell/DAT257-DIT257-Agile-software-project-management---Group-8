package com.backend.database;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;

/**
 * Composite primary key type for the Comments table.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-18
 */
@Embeddable
public class CommentKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    @Column(name="commentId")
    private int commentId;

    @Column(name="charity")
    private String charity;

    /**
     * Create a new comment key.
     * @param commentId Identifier of the comment.
     * @param charity Charity that was commented on.
     */
    public CommentKey(int commentId, String charity) {
        assert null != charity;
        this.commentId = commentId;
        this.charity = charity;
    }

    public int getCommentId() {
        return commentId;
    }

    public String getCharity() {
        return charity;
    }

    public void setCommentId(int id) {
        commentId = id;
    }

    public void setCharity(String charity) {
        assert null != charity;
        this.charity = charity;
    }
}

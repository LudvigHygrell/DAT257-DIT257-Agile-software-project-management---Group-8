package com.backend.database.entities.keys;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key type for the Comments table.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-18
 */
public class CommentKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    private Integer commentId;

    private String charity;

    protected CommentKey() {}

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

    @Override
    public String toString() {
        return String.format("CommentKey(id=%d, charity=%s)", commentId, charity);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof CommentKey &&
            ((CommentKey)object).commentId == commentId &&
            ((CommentKey)object).charity.equals(charity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, charity);
    }
}

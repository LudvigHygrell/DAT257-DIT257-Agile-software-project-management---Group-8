package com.backend.database;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents an entry in the Comments table.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-18
 */
@Entity
@Table(name="Comments")
public class Comment {

    @EmbeddedId
    private CommentKey key;

    @Column(name="comment")
    private JsonNode comment;

    @Column(name="commentUser")
    private String commentUser;

    protected Comment() {}

    /**
     * Create a new comment.
     * @param commentId Identifier of the comment.
     * @param charity Charity the comment is directed at.
     * @param comment The comment contents.
     * @param commentUser User that sent the comment.
     */
    public Comment(int commentId, String charity, JsonNode comment, String commentUser) {
        assert null != charity;
        assert null != comment;
        assert null != commentUser;
        this.key = new CommentKey(commentId, charity);
        this.comment = comment;
        this.commentUser = commentUser;
    }

    public int getCommentId() {
        return key.getCommentId();
    }

    public String getCharity() {
        return key.getCharity();
    }

    public JsonNode getComment() {
        return comment;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentId(int id) {
        this.key.setCommentId(id);
    }

    public void setCharity(String charity) {
        assert null != charity;
        this.key.setCharity(charity);
    }

    public void setComment(JsonNode comment) {
        assert null != comment;
        this.comment = comment;
    }

    public void setCommentUser(String user) {
        assert null != user;
        this.commentUser = user;
    }
}

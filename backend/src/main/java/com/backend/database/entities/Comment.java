package com.backend.database.entities;

import java.util.Objects;

import com.backend.database.entities.keys.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

/**
 * Represents an entry in the Comments table.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-18
 */
@Entity
@IdClass(CommentKey.class)
@Table(name="comments")
public class Comment {

    @Id
    @Column(name="charity")
    private String charity;

    @Id
    @Column(name="commentid")
    private int commentId;

    @Column(name="comment")
    private String comment;

    @Column(name="commentuser")
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
        this.commentId = commentId;
        this.charity = charity;
        this.comment = comment.toString();
        this.commentUser = commentUser;
    }

    public int getCommentId() {
        return commentId;
    }

    public String getCharity() {
        return charity;
    }

    public JsonNode getComment() {
        try {
            return new ObjectMapper().readTree(comment);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Unexpected processing error in validated JSON.");
        }
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentId(int id) {
        this.commentId = id;
    }

    public void setCharity(String charity) {
        assert null != charity;
        this.charity = charity;
    }

    public void setComment(JsonNode comment) {
        assert null != comment;
        this.comment = comment.toString();
    }

    public void setCommentUser(String user) {
        assert null != user;
        this.commentUser = user;
    }

    public JsonNode toJson() {
        return JsonNodeFactory.instance.objectNode()
            .put("charity", charity)
            .put("commentId", commentId)
            .put("comment", comment)
            .put("user", commentUser);
    }

    @Override
    public String toString() {
        return String.format("Comment(charity=%s, commentId=%d, comment=%s, commentUser=%s)", 
            getCharity(), getCommentId(), comment, commentUser);
    }
    
    @Override
    public boolean equals(Object object) {
        return object instanceof Comment &&
            ((Comment)object).getCharity().equals(getCharity()) &&
            ((Comment)object).getCommentId() == getCommentId() &&
            ((Comment)object).comment.equals(comment) &&
            ((Comment)object).commentUser.equals(commentUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommentId(), getCharity(), comment, commentUser);
    }
}

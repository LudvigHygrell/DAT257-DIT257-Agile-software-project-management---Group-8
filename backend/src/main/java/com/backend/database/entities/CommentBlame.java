package com.backend.database.entities;

import com.backend.database.entities.keys.CommentBlameKey;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

/**
 * Represents an entry into the CommentBlame table.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-18
 */
@Entity
@IdClass(CommentBlameKey.class)
@Table(name="commentblame")
public class CommentBlame implements GetMappedEntity {

    public static final String USER_COLUMN_NAME = "reporter";

    @Id
    @Column(name="commentid")
    private int commentId;

    @Id
    @Column(name="charity")
    private String charity;

    @Id
    @Column(name="reporter")
    private String reporter;

    @Column(name="reason")
    private String reason;

    protected CommentBlame() {}

    /**
     * Construct a new comment blame.
     * @param commentId Identifier of the blamed comment.
     * @param charity Charity the blamed comment was directed at.
     * @param reporter User that blamed the comment.
     * @param reason The reason for the blame.
     */
    public CommentBlame(int commentId, String charity,
                        String reporter, String reason) {
        assert null != charity;
        assert null != reporter;
        assert null != reason;

        this.commentId = commentId;
        this.charity = charity;
        this.reporter = reporter;
        this.reason = reason;
    }

    @Override
    public JsonNode toJson() {
        return JsonNodeFactory.instance.objectNode()
            .put("commentId", commentId)
            .put("charity", charity)
            .put("reporter", reporter)
            .put("reason", reason);
    }

    public int getCommentId() {
        return commentId;
    }

    public String getCharity() {
        return charity;
    }

    public String getReporter() {
        return reporter;
    }

    public String getReason() {
        return reason;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public void setCharity(String charity) {
        assert null != charity;
        this.charity = charity;
    }

    public void setReporter(String reporter) {
        assert null != reporter;
        this.reporter = reporter;
    }

    public void setReason(String reason) {
        assert null != reason;
        this.reason = reason;
    }
}

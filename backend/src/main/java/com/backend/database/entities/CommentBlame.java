package com.backend.database.entities;

import com.backend.database.entities.keys.*;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents an entry into the CommentBlame table.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-18
 */
@Entity
@Table(name="commentblame")
public class CommentBlame {

    @EmbeddedId
    private CommentBlameKey key;

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
        this.key = new CommentBlameKey(commentId, charity, reporter);
        this.reason = reason;
    }

    public int getCommentId() {
        return key.getCommentId();
    }

    public String getCharity() {
        return key.getCharity();
    }

    public String getReporter() {
        return key.getReporter();
    }

    public String getReason() {
        return reason;
    }

    public void setCommentId(int commentId) {
        this.key.setCommentId(commentId);
    }

    public void setCharity(String charity) {
        assert null != charity;
        this.key.setCharity(charity);
    }

    public void setReporter(String reporter) {
        assert null != reporter;
        this.key.setReporter(reporter);
    }

    public void setReason(String reason) {
        assert null != reason;
        this.reason = reason;
    }
}

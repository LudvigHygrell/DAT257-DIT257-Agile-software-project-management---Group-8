package com.backend.database.entities.keys;

import java.io.Serial;
import java.io.Serializable;

/**
 * Composite primary key into the CommentBlame table.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-18
 */
public class CommentBlameKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    private int commentId;
    private String charity;
    private String reporter;

    protected CommentBlameKey() {}

    /**
     * Construct a new comment blame key.
     * @param commentId Identifier of the blamed comment.
     * @param charity Charity that was commented on.
     * @param reporter User reporting the blame.
     */
    public CommentBlameKey(int commentId, String charity, String reporter) {
        assert null != charity;
        assert null != reporter;
        this.commentId = commentId;
        this.charity = charity;
        this.reporter = reporter;
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
}

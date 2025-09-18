package com.backend.database;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Restricted view into comment-related DB operations.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-18
 */
@Service
public class CommentsAdapter {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private CommentBlameRepository commentBlameRepository;

    /**
     * Register a new comment in the DB.
     * @param comment The comment contents to add.
     * @param user The commenting user.
     * @param charity Charity being commented on.
     * @return True if the comment was inserted.
     */
    public boolean add(JsonNode comment, String user, String charity) {
        try {
            int nextId = commentsRepository.getNextCommentId(charity);
            Comment record = new Comment(nextId, charity, comment, user);
            commentsRepository.save(record);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Remove a comment from the DB.
     * @param commentId Identifier of the comment to remove.
     * @param charity Charity that was commented on.
     * @return True if the comment was removed.
     */
    public boolean remove(int commentId, String charity) {
        try {
            commentsRepository.deleteById(new CommentKey(commentId, charity));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Register a blame sent by a user to a particular comment.
     * @param commentId Identifier of the blamed comment.
     * @param charity Charity that was commented on.
     * @param blamer User that sent the report.
     * @param reason Reason for blaming the comment.
     * @return True if the blame was successfully inserted.
     */
    public boolean blame(int commentId, String charity, String blamer, String reason) {
        try {
            commentBlameRepository.save(new CommentBlame(commentId, charity, blamer, reason));
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}

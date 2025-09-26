package com.backend.database.adapters;

import com.backend.database.repositories.*;
import com.backend.database.entities.*;
import com.backend.database.entities.keys.*;
import com.backend.database.filtering.Filter;
import com.backend.database.filtering.FilterBuilder;
import com.backend.database.filtering.FilteredQuery;
import com.backend.database.filtering.JsonToFilterConverter;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

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

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private CommentBlameRepository commentBlameRepository;

    @Autowired
    private NextCommentIdRepository nextCommentIdRepository;

    /**
     * Register a new comment in the DB.
     * @param comment The comment contents to add.
     * @param user The commenting user.
     * @param charity Charity being commented on.
     * @return True if the comment was inserted.
     */
    public boolean add(JsonNode comment, String user, String charity) {
        try {
            int nextId = nextCommentIdRepository.findById(charity)
                    .orElseThrow(() -> new RuntimeException("Failed to get next id.")).getId();
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

    /**
     * Get a list of all comments of a specific user.
     * @param forUser User to get all comments of.
     * @return The list of all comments.
     */
    public List<Comment> getComments(String forUser) {
        return commentsRepository.findAllByUser(forUser);
    }

    /**
     * Get a filtered list of comments.
     * @param forUser User that sent the comments.
     * @param filters Filters to apply.
     * @return The result of the query.
     */
    public List<Comment> getFilteredComments(String forUser, JsonNode filters) {

        FilteredQuery<Comment> query = new FilteredQuery<>(entityManager, Comment.class);
        FilterBuilder<Comment> fb = query.getFilterBuilder();
        Filter<Comment> filter = JsonToFilterConverter.filterFromJson(fb, filters);

        return query.runQuery(
            fb.and(List.of(
                fb.equalTo("commentUser", forUser),
                filter)));
    }
}

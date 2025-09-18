package com.backend.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository representing the Comments table.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-18
 */
public interface CommentsRepository extends JpaRepository<Comment, CommentKey> {

    /**
     * Gets the next available comment id for the specified charity.
     * @param charity charity to query the available comment id from.
     * @return The next available comment id.
     */
    @Query("SELECT commentId FROM NextCommentId WHERE charity=:charity")
    public int getNextCommentId(@Param("charity") String charity);
}

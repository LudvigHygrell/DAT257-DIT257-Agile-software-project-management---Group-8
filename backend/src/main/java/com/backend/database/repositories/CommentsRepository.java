package com.backend.database.repositories;

import com.backend.database.entities.*;
import com.backend.database.entities.keys.*;

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

}

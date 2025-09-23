package com.backend.database.repositories;

import com.backend.database.entities.*;
import com.backend.database.entities.keys.*;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository into the CommentBlame table.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-18
 */
public interface CommentBlameRepository extends JpaRepository<CommentBlame, CommentBlameKey> {

}

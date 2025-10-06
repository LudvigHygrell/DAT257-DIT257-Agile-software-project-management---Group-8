package com.backend.database.repositories;

import com.backend.database.entities.CommentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NextCommentIdRepository extends JpaRepository<CommentId, String> {

}

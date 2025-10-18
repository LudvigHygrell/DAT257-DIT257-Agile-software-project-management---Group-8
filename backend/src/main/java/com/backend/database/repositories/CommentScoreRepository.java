package com.backend.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.database.entities.CommentScore;
import com.backend.database.entities.keys.CommentBlameKey;

public interface CommentScoreRepository extends JpaRepository<CommentScore, CommentBlameKey> {
    
}

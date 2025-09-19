package com.backend.database;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.mapping.Selectable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for the Like table.
 * @author Ludvighygrell
 * @version 1.0
 * @since 2025-09-19
 */
public interface LikeRepository extends JpaRepository<Like, LikeKey> {

}

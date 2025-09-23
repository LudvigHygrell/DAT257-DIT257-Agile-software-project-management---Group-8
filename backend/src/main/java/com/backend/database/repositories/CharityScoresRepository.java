package com.backend.database.repositories;

import com.backend.database.entities.*;
import com.backend.database.entities.keys.*;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the Like table.
 * @author Ludvighygrell
 * @version 1.0
 * @since 2025-09-19
 */
public interface CharityScoresRepository extends JpaRepository<CharityVote, CharityVoteKey> {

}

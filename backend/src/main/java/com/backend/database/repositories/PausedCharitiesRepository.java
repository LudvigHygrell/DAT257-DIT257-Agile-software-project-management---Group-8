package com.backend.database.repositories;

import com.backend.database.entities.*;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository into the DB table signifying carities that are temporarily disabled.
 * @author JaarmaCo
 * @since 2025-09-23
 * @version 1.0
 */
public interface PausedCharitiesRepository extends JpaRepository<PausedCharity, String> {

}

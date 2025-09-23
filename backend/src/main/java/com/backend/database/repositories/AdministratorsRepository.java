package com.backend.database.repositories;

import com.backend.database.entities.*;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository into the Administrators DB table.
 * @author JaarmaCo
 * @version 1.0
 * @since 2025-09-23
 */
public interface AdministratorsRepository extends JpaRepository<Administrator, String> {
    
}

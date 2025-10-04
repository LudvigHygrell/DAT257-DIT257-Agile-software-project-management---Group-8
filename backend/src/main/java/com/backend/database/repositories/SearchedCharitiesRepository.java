package com.backend.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.database.entities.SearchedCharity;
import com.backend.database.entities.keys.SearchedCharityKey;

/**
 * Repository for list of previously searched charities.
 */
public interface SearchedCharitiesRepository extends JpaRepository<SearchedCharity, SearchedCharityKey> {
    
}

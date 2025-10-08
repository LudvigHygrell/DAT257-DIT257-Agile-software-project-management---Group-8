package com.backend.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.database.entities.CharityData;

public interface CharityDataRepository extends JpaRepository<String, CharityData> {
    
}

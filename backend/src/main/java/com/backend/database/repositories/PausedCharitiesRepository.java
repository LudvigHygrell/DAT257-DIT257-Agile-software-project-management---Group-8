package com.backend.database.repositories;

import com.backend.database.entities.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PausedCharitiesRepository extends JpaRepository<PausedCharity, String> {

}

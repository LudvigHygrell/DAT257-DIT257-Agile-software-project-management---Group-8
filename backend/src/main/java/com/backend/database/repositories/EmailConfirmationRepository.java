package com.backend.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.database.entities.EmailConfirmation;

public interface EmailConfirmationRepository extends JpaRepository<EmailConfirmation, String> {   
}

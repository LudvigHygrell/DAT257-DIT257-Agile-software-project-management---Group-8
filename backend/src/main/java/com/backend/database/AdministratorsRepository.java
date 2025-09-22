package com.backend.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorsRepository extends JpaRepository<Administrator, String> {
    
}

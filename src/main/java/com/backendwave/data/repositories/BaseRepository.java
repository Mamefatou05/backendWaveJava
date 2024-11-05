package com.backendwave.data.repositories;

import com.backendwave.data.entities.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
    // Méthodes communes à tous les repositories peuvent être ajoutées ici
} 
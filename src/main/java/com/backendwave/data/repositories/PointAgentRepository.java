package com.backendwave.data.repositories;

import com.backendwave.data.entities.PointAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PointAgentRepository extends JpaRepository<PointAgent, Long> {

    // Trouver tous les points agents actifs
    List<PointAgent> findByEstActifTrue();

    // Trouver par adresse (recherche exacte)
    Optional<PointAgent> findByAdresse(String adresse);

}
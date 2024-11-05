package com.backendwave.data.repositories;

import com.backendwave.data.entities.Marchand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarchandRepository extends JpaRepository<Marchand, Long> {


    // Trouver par utilisateur id
    Optional<Marchand> findByUtilisateurId(Long utilisateurId);
    // Trouver par nom commercial
    Optional<Marchand> findByNomCommercial(String nomCommercial);
    
    // Existe par utilisateur id
    boolean existsByUtilisateurId(Long utilisateurId);
} 
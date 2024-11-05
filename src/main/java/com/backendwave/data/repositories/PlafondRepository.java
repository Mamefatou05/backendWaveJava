package com.backendwave.data.repositories;

import com.backendwave.data.entities.Plafond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlafondRepository extends JpaRepository<Plafond, Long> {
    
    // Trouver le plafond par ID utilisateur
    Optional<Plafond> findByUtilisateur_Id(Long utilisateurId);

} 
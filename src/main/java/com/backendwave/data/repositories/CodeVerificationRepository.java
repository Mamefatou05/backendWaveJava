package com.backendwave.data.repositories;

import com.backendwave.data.entities.CodeVerification;
import com.backendwave.data.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CodeVerificationRepository extends JpaRepository<CodeVerification, Long> {
    
    // Recherche par code et non utilisé
    Optional<CodeVerification> findByCodeAndEstUtiliseFalse(String code);
    
    // Recherche par utilisateur
    List<CodeVerification> findByUtilisateur(Utilisateur utilisateur);
    
    // Vérifier si un code existe et n'est pas utilisé
    boolean existsByCodeAndEstUtiliseFalse(String code);
} 
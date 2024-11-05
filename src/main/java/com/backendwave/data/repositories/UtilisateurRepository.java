package com.backendwave.data.repositories;

import com.backendwave.data.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    
    // Recherche par numéro de téléphone
    Optional<Utilisateur> findByNumeroTelephone(String numeroTelephone);
    
    // Recherche par email
    Optional<Utilisateur> findByEmail(String email);
    
    // Recherche par rôle
    List<Utilisateur> findByRole_Id(Long roleId);
    
    // Recherche des utilisateurs actifs
    List<Utilisateur> findByEstActifTrue();
    
    // Vérifier si un numéro de téléphone existe
    boolean existsByNumeroTelephone(String numeroTelephone);
    
    // Vérifier si un email existe
    boolean existsByEmail(String email);
}

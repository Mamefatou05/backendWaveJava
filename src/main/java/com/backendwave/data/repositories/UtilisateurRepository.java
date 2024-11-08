package com.backendwave.data.repositories;

import com.backendwave.data.entities.Utilisateur;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
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
    List<Utilisateur> findByRoleId(Long roleId);

    // Recherche des utilisateurs actifs
    List<Utilisateur> findByEstActifTrue();

    // Vérifier si un numéro de téléphone existe
    boolean existsByNumeroTelephone(String numeroTelephone);

    // Vérifier si un email existe
    boolean existsByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM Utilisateur u WHERE u.numeroTelephone = :numeroTelephone")
    Optional<Utilisateur> findByNumeroTelephoneForUpdate(String numeroTelephone);

}
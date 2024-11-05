package com.backendwave.data.repositories;

import com.backendwave.data.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Trouver un rôle par son nom (recherche exacte)
    Optional<Role> findByNom(String nom);

    // Trouver des rôles par nom (recherche partielle, insensible à la casse)
    List<Role> findByNomContainingIgnoreCase(String nom);

    // Vérifier si un rôle existe par son nom
    boolean existsByNom(String nom);
}
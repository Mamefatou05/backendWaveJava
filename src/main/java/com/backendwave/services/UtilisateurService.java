package com.backendwave.services;

import com.backendwave.data.entities.Utilisateur;
import java.util.List;
import java.util.Optional;

public interface UtilisateurService {
    Utilisateur save(Utilisateur utilisateur);
    Optional<Utilisateur> findById(Long id);
    List<Utilisateur> findAll();
    void deleteById(Long id);
    Optional<Utilisateur> findByNumeroTelephone(String numeroTelephone);
    Optional<Utilisateur> findByEmail(String email);
    List<Utilisateur> findByRoleId(Long roleId);
    List<Utilisateur> findActiveUsers();
    boolean existsByNumeroTelephone(String numeroTelephone);
    boolean existsByEmail(String email);
}

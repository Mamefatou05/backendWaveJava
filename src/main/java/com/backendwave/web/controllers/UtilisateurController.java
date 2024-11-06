package com.backendwave.web.controllers;

import com.backendwave.data.entities.Utilisateur;
import com.backendwave.web.dto.request.users.CreateClientDto;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

public interface UtilisateurController {
    // Méthodes existantes
    void createUser(Utilisateur utilisateur);
    Optional<Utilisateur> getUserById(Long id);
    List<Utilisateur> getAllUsers();
    void deleteUserById(Long id);
    Optional<Utilisateur> getUserByNumeroTelephone(String numeroTelephone);
    Optional<Utilisateur> getUserByEmail(String email);
    List<Utilisateur> getUsersByRoleId(Long roleId);
    List<Utilisateur> getActiveUsers();

    // Nouvelle méthode pour la création spécifique d'un client
    ResponseEntity<Utilisateur> createClient(CreateClientDto createClientDto);
}
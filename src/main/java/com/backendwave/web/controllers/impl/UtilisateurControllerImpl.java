package com.backendwave.web.controllers.impl;

import com.backendwave.web.controllers.UtilisateurController;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.services.UtilisateurService;
import com.backendwave.web.dto.request.users.CreateClientDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UtilisateurControllerImpl implements UtilisateurController {

   private final UtilisateurService utilisateurService;

   @Autowired
   public UtilisateurControllerImpl(UtilisateurService utilisateurService) {
       this.utilisateurService = utilisateurService;
   }

   @Override
   @PostMapping("/register/client")
   public ResponseEntity<Utilisateur> createClient(@Valid @RequestBody CreateClientDto createClientDto) {
       try {
           Utilisateur client = utilisateurService.createClient(createClientDto);
           return ResponseEntity.ok(client);
       } catch (IllegalArgumentException e) {
           return ResponseEntity.badRequest().build();
       } catch (Exception e) {
           return ResponseEntity.internalServerError().build();
       }
   }

   @Override
   @PostMapping
   public void createUser(@RequestBody Utilisateur utilisateur) {
       utilisateurService.save(utilisateur);
   }

   @Override
   @GetMapping("/{id}")
   public Optional<Utilisateur> getUserById(@PathVariable Long id) {
       return utilisateurService.findById(id);
   }

   @Override
   @GetMapping
   public List<Utilisateur> getAllUsers() {
       return utilisateurService.findAll();
   }

   @Override
   @DeleteMapping("/{id}")
   public void deleteUserById(@PathVariable Long id) {
       utilisateurService.deleteById(id);
   }

   @Override
   @GetMapping("/telephone/{numeroTelephone}")
   public Optional<Utilisateur> getUserByNumeroTelephone(@PathVariable String numeroTelephone) {
       return utilisateurService.findByNumeroTelephone(numeroTelephone);
   }

   @Override
   @GetMapping("/email/{email}")
   public Optional<Utilisateur> getUserByEmail(@PathVariable String email) {
       return utilisateurService.findByEmail(email);
   }

   @Override
   @GetMapping("/role/{roleId}")
   public List<Utilisateur> getUsersByRoleId(@PathVariable Long roleId) {
       return utilisateurService.findByRoleId(roleId);
   }

   @Override
   @GetMapping("/active")
   public List<Utilisateur> getActiveUsers() {
       return utilisateurService.findActiveUsers();
   }
}
package com.backendwave.web.controllers.impl;

import com.backendwave.data.entities.NumeroFavori;
import com.backendwave.services.NumeroFavoriService;
import com.backendwave.web.controllers.NumeroFavoriController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/favoris")
public class NumeroFavoriControllerImpl implements NumeroFavoriController {

    private final NumeroFavoriService numeroFavoriService;

    @Autowired
    public NumeroFavoriControllerImpl(NumeroFavoriService numeroFavoriService) {
        this.numeroFavoriService = numeroFavoriService;
    }

    @GetMapping("/{clientId}")
    public List<NumeroFavori> getAllNumerosFavoris(@PathVariable Long clientId) {
        log.info("=== RÉCUPÉRATION DES FAVORIS ===");
        log.info("ClientID: {}", clientId);

        List<NumeroFavori> favoris = numeroFavoriService.getAllNumerosFavoris(clientId);

        log.info("📱 Nombre de favoris trouvés: {}", favoris.size());
        if (!favoris.isEmpty()) {
            log.info("Liste des favoris:");
            favoris.forEach(f -> log.info("- {} ({})", f.getNumeroTelephone(), f.getNom() != null ? f.getNom() : "Sans nom"));
        } else {
            log.info("Aucun favori trouvé");
        }

        return favoris;
    }

    @PostMapping("/{clientId}")
    public NumeroFavori ajouterNumeroFavori(
            @PathVariable Long clientId,
            @RequestParam String numeroTelephone,
            @RequestParam(required = false) String nom) {
        log.info("=== AJOUT DE FAVORI ===");
        log.info("ClientID: {}", clientId);
        log.info("Numéro de téléphone: {}", numeroTelephone);
        log.info("Nom: {}", nom);

        try {
            NumeroFavori nouveauFavori = numeroFavoriService.ajouterNumeroFavori(clientId, numeroTelephone, nom);
            log.info("✅ Favori ajouté avec succès: ID {}", nouveauFavori.getId());
            return nouveauFavori;
        } catch (IllegalArgumentException e) {
            log.error("❌ Erreur lors de l'ajout du favori: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ Erreur inattendue lors de l'ajout du favori: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de l'ajout du favori", e);
        }
    }

    @DeleteMapping("/{clientId}")
    public void supprimerNumeroFavori(
            @PathVariable Long clientId,
            @RequestParam String numeroTelephone) {
        log.info("=== SUPPRESSION DE FAVORI ===");
        log.info("ClientID: {}", clientId);
        log.info("Numéro de téléphone: {}", numeroTelephone);

        try {
            numeroFavoriService.supprimerNumeroFavori(clientId, numeroTelephone);
            log.info("✅ Favori supprimé avec succès");
        } catch (IllegalArgumentException e) {
            log.error("❌ Erreur lors de la suppression du favori: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ Erreur inattendue lors de la suppression du favori: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la suppression du favori", e);
        }
    }
}
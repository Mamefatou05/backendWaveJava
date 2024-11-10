package com.backendwave.services.impl;

import com.backendwave.data.entities.NumeroFavori;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.repositories.NumeroFavoriRepository;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.backendwave.services.NumeroFavoriService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class NumeroFavoriServiceImpl implements NumeroFavoriService {

    private final NumeroFavoriRepository numeroFavoriRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public NumeroFavoriServiceImpl(NumeroFavoriRepository numeroFavoriRepository, UtilisateurRepository utilisateurRepository) {
        this.numeroFavoriRepository = numeroFavoriRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public List<NumeroFavori> getAllNumerosFavoris(Long clientId) {
        log.info("=== RÉCUPÉRATION DES FAVORIS ===");
        log.info("ClientID: {}", clientId);

        // Vérifier si le client existe
        Optional<Utilisateur> client = utilisateurRepository.findById(clientId);
        if (client.isEmpty()) {
            log.error("❌ Client non trouvé avec l'ID: {}", clientId);
            throw new IllegalArgumentException("Client non trouvé");
        }
        log.info("✅ Client trouvé: {}", client.get().getNomComplet());

        // Récupérer les favoris
        log.info("Récupération des numéros favoris du client...");
        List<NumeroFavori> favoris = numeroFavoriRepository.findByClient_Id(clientId);
        log.info("📱 Nombre de favoris trouvés: {}", favoris.size());

        // Logger chaque favori
        if (!favoris.isEmpty()) {
            log.info("Liste des favoris:");
            favoris.forEach(f -> log.info("- {} ({})", f.getNumeroTelephone(), f.getNom() != null ? f.getNom() : "Sans nom"));
        } else {
            log.info("Aucun favori trouvé");
        }

        return favoris;
    }

    @Override
    public Optional<NumeroFavori> getNumeroFavori(Long clientId, String numeroTelephone) {
        return numeroFavoriRepository.findByClient_IdAndNumeroTelephone(clientId, numeroTelephone);
    }

    @Override
    public NumeroFavori ajouterNumeroFavori(Long clientId, String numeroTelephone, String nom) {
        log.info("=== AJOUT NOUVEAU FAVORI ===");
        log.info("ClientID: {}", clientId);
        log.info("Numéro: {}", numeroTelephone);
        log.info("Nom: {}", nom != null ? nom : "Non spécifié");

        // Vérifier si le client existe
        Optional<Utilisateur> client = utilisateurRepository.findById(clientId);
        if (client.isEmpty()) {
            log.error("❌ Client non trouvé avec l'ID: {}", clientId);
            throw new IllegalArgumentException("Client non trouvé");
        }
        log.info("✅ Client trouvé: {}", client.get().getNomComplet());

        // Vérifier si le numéro existe parmi les utilisateurs
        log.info("Vérification de l'existence du numéro parmi les utilisateurs...");
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByNumeroTelephone(numeroTelephone);
        if (utilisateurOpt.isEmpty()) {
            log.error("❌ Le numéro de téléphone n'existe pas parmi les utilisateurs: {}", numeroTelephone);
            throw new IllegalArgumentException("Le numéro de téléphone n'existe pas parmi les utilisateurs.");
        }
        log.info("✅ Le numéro de téléphone existe parmi les utilisateurs: {}", utilisateurOpt.get().getNomComplet());

        // Vérifier si le numéro favori existe déjà pour le client
        log.info("Vérification de l'existence du numéro favori pour le client...");
        Optional<NumeroFavori> existant = numeroFavoriRepository.findByClient_IdAndNumeroTelephone(clientId, numeroTelephone);
        if (existant.isPresent()) {
            log.error("❌ Le numéro de téléphone est déjà dans les favoris: {}", numeroTelephone);
            throw new IllegalArgumentException("Ce numéro est déjà dans les favoris.");
        }
        log.info("✅ Le numéro de téléphone n'est pas encore dans les favoris");

        // Créer le nouveau favori
        log.info("Création du nouveau favori...");
        NumeroFavori nouveauFavori = new NumeroFavori();
        nouveauFavori.setClient(client.get());
        log.info("Client assigné: {}", client.get().getNomComplet());
        nouveauFavori.setNumeroTelephone(numeroTelephone);
        log.info("Numéro de téléphone: {}", numeroTelephone);
        nouveauFavori.setNom(nom);
        log.info("Nom: {}", nom);

        // Sauvegarder
        log.info("Sauvegarde du nouveau favori...");
        NumeroFavori saved = numeroFavoriRepository.save(nouveauFavori);
        log.info("✅ Favori ajouté avec succès: ID {}", saved.getId());

        return saved;
    }
    @Override
    public void supprimerNumeroFavori(Long clientId, String numeroTelephone) {
        log.info("=== SUPPRESSION DE FAVORI ===");
        log.info("ClientID: {}", clientId);
        log.info("Numéro: {}", numeroTelephone);

        // Vérifier si le favori existe
        log.info("Recherche du favori à supprimer...");
        Optional<NumeroFavori> favori = numeroFavoriRepository.findByClient_IdAndNumeroTelephone(clientId, numeroTelephone);
        if (favori.isEmpty()) {
            log.error("❌ Favori non trouvé");
            throw new IllegalArgumentException("Favori non trouvé");
        }

        // Supprimer
        log.info("Suppression du favori...");
        numeroFavoriRepository.delete(favori.get());
        log.info("✅ Favori supprimé avec succès");
    }
}
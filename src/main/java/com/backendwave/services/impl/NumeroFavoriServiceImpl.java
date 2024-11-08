
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
        System.out.print("azerty");
        System.out.println(clientId);
       List<NumeroFavori> Numero =   numeroFavoriRepository.findByClient_Id(clientId);
       System.out.println(Numero);
        return Numero ;
    }

    @Override
    public Optional<NumeroFavori> getNumeroFavori(Long clientId, String numeroTelephone) {
        return numeroFavoriRepository.findByClient_IdAndNumeroTelephone(clientId, numeroTelephone);
    }

    @Override
    public NumeroFavori ajouterNumeroFavori(Long clientId, String numeroTelephone, String nom) {
        // Vérifier si le numéro existe parmi les utilisateurs
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByNumeroTelephone(numeroTelephone);
        if (utilisateurOpt.isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone n'existe pas parmi les utilisateurs.");
        }

        // Vérifier si le numéro favori existe déjà pour le client
        Optional<NumeroFavori> existant = numeroFavoriRepository.findByClient_IdAndNumeroTelephone(clientId, numeroTelephone);
        if (existant.isPresent()) {
            throw new IllegalArgumentException("Ce numéro est déjà dans les favoris.");
        }

        NumeroFavori nouveauFavori = new NumeroFavori();
        nouveauFavori.setClient(utilisateurOpt.get());
        nouveauFavori.setNumeroTelephone(numeroTelephone);
        nouveauFavori.setNom(nom);

        return numeroFavoriRepository.save(nouveauFavori);
    }

    @Override
    public void supprimerNumeroFavori(Long clientId, String numeroTelephone) {
        Optional<NumeroFavori> favori = numeroFavoriRepository.findByClient_IdAndNumeroTelephone(clientId, numeroTelephone);
        favori.ifPresent(numeroFavoriRepository::delete);
    }
}

package com.backendwave.services;

import com.backendwave.data.entities.NumeroFavori;
import java.util.List;
import java.util.Optional;

public interface NumeroFavoriService {

    List<NumeroFavori> getAllNumerosFavoris(Long clientId);

    Optional<NumeroFavori> getNumeroFavori(Long clientId, String numeroTelephone);

    NumeroFavori ajouterNumeroFavori(Long clientId, String numeroTelephone, String nom);

    void supprimerNumeroFavori(Long clientId, String numeroTelephone);
}

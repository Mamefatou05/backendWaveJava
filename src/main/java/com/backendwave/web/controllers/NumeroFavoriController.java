package com.backendwave.web.controllers;

import com.backendwave.data.entities.NumeroFavori;

import java.util.List;

public interface NumeroFavoriController {

    List<NumeroFavori> getAllNumerosFavoris(Long clientId);

    NumeroFavori ajouterNumeroFavori(Long clientId, String numeroTelephone, String nom);

    void supprimerNumeroFavori(Long clientId, String numeroTelephone);
}

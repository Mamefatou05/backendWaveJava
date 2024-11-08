package com.backendwave.web.controllers.impl;

import com.backendwave.data.entities.NumeroFavori;
import com.backendwave.services.NumeroFavoriService;
import com.backendwave.web.controllers.NumeroFavoriController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoris")
public class NumeroFavoriControllerImpl implements NumeroFavoriController {

    private final NumeroFavoriService numeroFavoriService;

    @Autowired
    public NumeroFavoriControllerImpl(NumeroFavoriService numeroFavoriService) {
        this.numeroFavoriService = numeroFavoriService;
    }

    @GetMapping("/{clientId}")
    public List<NumeroFavori> getAllNumerosFavoris(@PathVariable Long clientId) {
        return numeroFavoriService.getAllNumerosFavoris(clientId);
    }

    @PostMapping("/{clientId}")
    public NumeroFavori ajouterNumeroFavori(
            @PathVariable Long clientId,
            @RequestParam String numeroTelephone,
            @RequestParam(required = false) String nom) {
        return numeroFavoriService.ajouterNumeroFavori(clientId, numeroTelephone, nom);
    }

    @DeleteMapping("/{clientId}")
    public void supprimerNumeroFavori(
            @PathVariable Long clientId,
            @RequestParam String numeroTelephone) {
        numeroFavoriService.supprimerNumeroFavori(clientId, numeroTelephone);
    }
}

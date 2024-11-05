package com.backendwave.services;

import com.backendwave.data.entities.Plafond;
import java.util.List;
import java.util.Optional;

public interface PlafondService {
    Plafond save(Plafond plafond);
    Plafond findById(Long id);
    List<Plafond> findAll();
    void deleteById(Long id);
    Optional<Plafond> findByUtilisateurId(Long utilisateurId);
}

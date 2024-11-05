package com.backendwave.data.repositories;

import com.backendwave.data.entities.NumeroFavori;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NumeroFavoriRepository extends BaseRepository<NumeroFavori> {
    
    // Trouver tous les numéros favoris d'un client
    List<NumeroFavori> findByClientId(Long clientId);
    
    // Trouver un numéro favori spécifique d'un client
    Optional<NumeroFavori> findByClientIdAndNumeroTelephone(Long clientId, String numeroTelephone);
} 
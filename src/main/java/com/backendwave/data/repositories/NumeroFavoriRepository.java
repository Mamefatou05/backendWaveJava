package com.backendwave.data.repositories;

import com.backendwave.data.entities.NumeroFavori;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NumeroFavoriRepository extends BaseRepository<NumeroFavori> {


    List<NumeroFavori> findByClient_Id(Long clientId);

    Optional<NumeroFavori> findByClient_IdAndNumeroTelephone(Long clientId, String numeroTelephone);
}
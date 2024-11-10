package com.backendwave.data.repositories;

import com.backendwave.data.entities.NumeroFavori;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NumeroFavoriRepository extends BaseRepository<NumeroFavori> {

    @Query("SELECT nf FROM NumeroFavori nf WHERE nf.client.id = :clientId")
    List<NumeroFavori> findByClient_Id(@Param("clientId") Long clientId);

    @Query("SELECT n FROM NumeroFavori n JOIN FETCH n.client c WHERE c.id = :clientId AND n.numeroTelephone = :numeroTelephone")
    Optional<NumeroFavori> findByClient_IdAndNumeroTelephone(@Param("clientId") Long clientId, @Param("numeroTelephone") String numeroTelephone);

    @Query("SELECT nf, u.id, u.nomComplet FROM NumeroFavori nf JOIN nf.client u")
    List<Object[]> findAllNumerosFavoriWithClient();
}
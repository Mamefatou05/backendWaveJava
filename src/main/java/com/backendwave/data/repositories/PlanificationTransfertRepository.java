package com.backendwave.data.repositories;

import com.backendwave.data.entities.PlanificationTransfert;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlanificationTransfertRepository extends BaseRepository<PlanificationTransfert> {

    @Query("SELECT p FROM PlanificationTransfert p JOIN FETCH p.expediteur e JOIN FETCH p.destinataire d WHERE p.prochaineExecution < :currentDate AND p.estActif = true")
    List<PlanificationTransfert> findByProchaineExecutionBeforeAndEstActif(@Param("currentDate") LocalDateTime currentDate);
}

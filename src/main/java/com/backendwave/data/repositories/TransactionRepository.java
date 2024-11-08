package com.backendwave.data.repositories;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByExpediteur_Id(Long expediteurId);

    List<Transaction> findByDestinataire_Id(Long destinataireId);

    List<Transaction> findByStatut(TransactionStatus statut);

    List<Transaction> findByTypeTransaction(TransactionType type);

    List<Transaction> findByExpediteurOrDestinataire(Utilisateur expediteur, Utilisateur destinataire);

    List<Transaction> findByEstPlanifieTrueAndProchaineExecutionLessThanEqual(LocalDateTime dateTime);

    List<Transaction> findByExpediteur_IdAndDateCreationBetween(Long expediteurId, LocalDateTime debut, LocalDateTime fin);
}
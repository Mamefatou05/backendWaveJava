package com.backendwave.data.repositories;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.data.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Trouver les transactions par expéditeur
    List<Transaction> findByExpediteur_Id(Long expediteurId);

    // Trouver les transactions par destinataire
    List<Transaction> findByDestinataire_Id(Long destinataireId);

    // Trouver par statut
    List<Transaction> findByStatut(TransactionStatus statut);

    List<Transaction> findByStatutAndDateCreationLessThanOrderByDateCreationAsc(
        TransactionStatus statut,
        LocalDateTime dateCreation
    );

    @Query("SELECT t FROM Transaction t " +
            "WHERE (t.expediteur.id = :userId OR t.destinataire.id = :userId) " +
            "AND (:type IS NULL OR t.typeTransaction = :type) " +
            "ORDER BY t.dateCreation DESC")
    List<Transaction> findUserTransactionsWithFilter(
            @Param("userId") Long userId,
            @Param("type") TransactionType type
    );

    // Trouver les transactions planifiées à exécuter
//   List<Transaction> findByEstPlanifieTrueAndProchaineExecutionLessThanEqual(LocalDateTime dateTime);


   List<Transaction> findByExpediteur_IdAndDateCreationBetween(Long expediteurId, LocalDateTime debut, LocalDateTime fin);

    // Trouver par type de transaction
    List<Transaction> findByTypeTransaction(TransactionType type);

    // Trouver toutes les transactions d'un utilisateur (expéditeur ou destinataire)
    List<Transaction> findByExpediteur_IdOrDestinataire_Id(Long expediteurId, Long destinataireId);

    // Trouver toutes les transactions d'un utilisateur (expéditeur ou destinataire) par numéro de téléphone
    List<Transaction> findByExpediteurOrDestinataire(Utilisateur utilisateurExpediteur, Utilisateur utilisateurDestinataire);
}
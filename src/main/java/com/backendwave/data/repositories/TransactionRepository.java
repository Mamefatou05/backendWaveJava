package com.backendwave.data.repositories;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
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
    
    // Trouver les transactions planifiées à exécuter
    List<Transaction> findByEstPlanifieTrueAndProchaineExecutionLessThanEqual(LocalDateTime dateTime);
    
    // Trouver par type de transaction
    List<Transaction> findByTypeTransaction(TransactionType type);
    
    // Trouver toutes les transactions d'un utilisateur (expéditeur ou destinataire)
    List<Transaction> findByExpediteur_IdOrDestinataire_Id(Long expediteurId, Long destinataireId);
} 
package com.backendwave.services;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    Transaction save(Transaction transaction);
    Transaction findById(Long id);
    List<Transaction> findAll();
    void deleteById(Long id);
    List<Transaction> findByExpediteurId(Long expediteurId);
    List<Transaction> findByDestinataireId(Long destinataireId);
    List<Transaction> findByStatut(TransactionStatus statut);
    List<Transaction> findPlannedTransactions(LocalDateTime dateTime);
    List<Transaction> findByTypeTransaction(TransactionType type);
    List<Transaction> findByUserId(Long userId);
}

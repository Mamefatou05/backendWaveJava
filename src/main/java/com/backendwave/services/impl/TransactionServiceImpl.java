package com.backendwave.services.impl;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.data.repositories.TransactionRepository;
import com.backendwave.services.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public List<Transaction> findByExpediteurId(Long expediteurId) {
        return transactionRepository.findByExpediteur_Id(expediteurId);
    }

    @Override
    public List<Transaction> findByDestinataireId(Long destinataireId) {
        return transactionRepository.findByDestinataire_Id(destinataireId);
    }

    @Override
    public List<Transaction> findByStatut(TransactionStatus statut) {
        return transactionRepository.findByStatut(statut);
    }

    @Override
    public List<Transaction> findPlannedTransactions(LocalDateTime dateTime) {
        return transactionRepository.findByEstPlanifieTrueAndProchaineExecutionLessThanEqual(dateTime);
    }



    @Override
    public List<Transaction> findByTypeTransaction(TransactionType type) {
        return transactionRepository.findByTypeTransaction(type);
    }

    @Override
    public List<Transaction> findByUserId(Long userId) {
        return transactionRepository.findByExpediteur_IdOrDestinataire_Id(userId, userId);
    }
}

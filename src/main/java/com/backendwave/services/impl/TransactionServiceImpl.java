package com.backendwave.services.impl;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.data.repositories.TransactionRepository;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.backendwave.services.TransactionService;
import com.backendwave.web.dto.request.transactions.TransferRequestDto;
import com.backendwave.web.dto.response.transactions.TransferResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

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
    public List<Transaction> findBySenderPhoneNumber(String senderPhoneNumber) {
        Utilisateur sender = utilisateurRepository.findByNumeroTelephone(senderPhoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Expéditeur introuvable"));
        return transactionRepository.findByExpediteur_Id(sender.getId());
    }

    @Override
    public List<Transaction> findByRecipientPhoneNumber(String recipientPhoneNumber) {
        Utilisateur recipient = utilisateurRepository.findByNumeroTelephone(recipientPhoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Destinataire introuvable"));
        return transactionRepository.findByDestinataire_Id(recipient.getId());
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
    public List<Transaction> findByUserPhoneNumber(String phoneNumber) {
        Utilisateur user = utilisateurRepository.findByNumeroTelephone(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        return transactionRepository.findByExpediteurOrDestinataire(user, user);
    }

    @Override
    public TransferResponseDto transfer(TransferRequestDto transferRequestDto) {
        // Récupérer l'expéditeur et le destinataire à partir des numéros de téléphone
        Utilisateur sender = utilisateurRepository.findByNumeroTelephone(transferRequestDto.getSenderPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("Expéditeur introuvable"));
        Utilisateur recipient = utilisateurRepository.findByNumeroTelephone(transferRequestDto.getRecipientPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("Destinataire introuvable"));

        // Créer une nouvelle transaction
        Transaction transaction = new Transaction();
        transaction.setExpediteur(sender);
        transaction.setDestinataire(recipient);
        transaction.setMontant(transferRequestDto.getAmount());
        transaction.setTypeTransaction(TransactionType.TRANSFERT);
        transaction.setStatut(TransactionStatus.EN_ATTENTE);
        transaction.setEstPlanifie(transferRequestDto.getPeriodicity() != null);
        transaction.setPeriodicite(transferRequestDto.getPeriodicity());
        transaction.setReferenceGroupe(transferRequestDto.getGroupReference());

        // Calculer les frais de transfert (supposons 1% du montant)
        BigDecimal transferFee = transaction.getMontant().multiply(new BigDecimal("0.01"));
        transaction.setFraisTransfert(transferFee);

        // Enregistrer la transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Retourner une réponse
        TransferResponseDto responseDto = new TransferResponseDto();
        responseDto.setTransactionId(savedTransaction.getId());
        responseDto.setStatus(savedTransaction.getStatut());
        responseDto.setTransferFee(savedTransaction.getFraisTransfert());
        responseDto.setTotalAmount(transaction.getMontant().add(transferFee));
        return responseDto;
    }
}
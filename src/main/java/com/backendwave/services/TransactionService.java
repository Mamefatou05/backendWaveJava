package com.backendwave.services;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.web.dto.request.transactions.CancelTransactionRequestDto;
import com.backendwave.web.dto.request.transactions.MultipleTransferRequestDto;
import com.backendwave.web.dto.request.transactions.TransferRequestDto;
import com.backendwave.web.dto.response.transactions.CancelTransactionResponseDto;
import com.backendwave.web.dto.response.transactions.TransactionListDto;
import com.backendwave.web.dto.response.transactions.TransferResponseDto;

import java.util.List;

public interface TransactionService {
    // Opérations CRUD de base
    Transaction save(Transaction transaction);
    Transaction findById(Long id);
    List<Transaction> findAll();
    void deleteById(Long id);

    // Opérations de recherche
    List<Transaction> findBySenderPhoneNumber(String senderPhoneNumber);
    List<Transaction> findByRecipientPhoneNumber(String recipientPhoneNumber);
    List<Transaction> findByStatut(TransactionStatus statut);
    List<Transaction> findByTypeTransaction(TransactionType type);
    List<Transaction> findByUserPhoneNumber(String phoneNumber);
    List<TransactionListDto> getUserTransactions(Long userId, TransactionType type);

    // Opérations de transfert
    TransferResponseDto transfer(TransferRequestDto transferRequestDto);
    List<TransferResponseDto> multipleTransfer(MultipleTransferRequestDto requestDto);
    CancelTransactionResponseDto cancelTransfer(CancelTransactionRequestDto cancelRequest);
}
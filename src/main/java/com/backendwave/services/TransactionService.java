package com.backendwave.services;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.web.dto.request.transactions.CancelTransactionRequestDto;
import com.backendwave.web.dto.request.transactions.TransferRequestDto;
import com.backendwave.web.dto.response.transactions.CancelTransactionResponseDto;
import com.backendwave.web.dto.response.transactions.TransferResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    Transaction save(Transaction transaction);

    Transaction findById(Long id);

    List<Transaction> findAll();

    void deleteById(Long id);

    List<Transaction> findBySenderPhoneNumber(String senderPhoneNumber);

    List<Transaction> findByRecipientPhoneNumber(String recipientPhoneNumber);

    List<Transaction> findByStatut(TransactionStatus statut);

    List<Transaction> findPlannedTransactions(LocalDateTime dateTime);

    List<Transaction> findByTypeTransaction(TransactionType type);

    List<Transaction> findByUserPhoneNumber(String phoneNumber);

    TransferResponseDto transfer(TransferRequestDto transferRequestDto);

    CancelTransactionResponseDto cancelTransfer(CancelTransactionRequestDto cancelRequest);
}
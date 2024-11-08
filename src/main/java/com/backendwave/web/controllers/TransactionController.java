package com.backendwave.web.controllers;

import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.web.dto.request.transactions.CancelTransactionRequestDto;
import com.backendwave.web.dto.request.transactions.TransferRequestDto;
import com.backendwave.web.dto.response.transactions.CancelTransactionResponseDto;
import com.backendwave.web.dto.response.transactions.TransferResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionController {
    TransferResponseDto transfer(TransferRequestDto transferRequestDto);
    List<TransferResponseDto> findBySenderPhoneNumber(String senderPhoneNumber);
    List<TransferResponseDto> findByRecipientPhoneNumber(String recipientPhoneNumber);
    List<TransferResponseDto> findByStatus(TransactionStatus status);
    List<TransferResponseDto> findPlannedTransactions(LocalDateTime dateTime);
    List<TransferResponseDto> findByTransactionType(TransactionType type);
    List<TransferResponseDto> findByUserPhoneNumber(String phoneNumber);
    CancelTransactionResponseDto cancelTransfer(CancelTransactionRequestDto cancelRequest);

}
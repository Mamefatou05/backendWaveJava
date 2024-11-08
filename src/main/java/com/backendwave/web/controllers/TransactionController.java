package com.backendwave.web.controllers;

import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.web.dto.request.transactions.CancelTransactionRequestDto;
import com.backendwave.web.dto.request.transactions.MultipleTransferRequestDto;
import com.backendwave.web.dto.request.transactions.TransferRequestDto;
import com.backendwave.web.dto.response.transactions.CancelTransactionResponseDto;
import com.backendwave.web.dto.response.transactions.TransferResponseDto;
import com.backendwave.web.dto.response.transactions.TransactionListDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TransactionController {
    TransferResponseDto transfer(@RequestBody TransferRequestDto transferRequestDto);

    ResponseEntity<List<TransferResponseDto>> multipleTransfer(@RequestBody MultipleTransferRequestDto requestDto);

    List<TransferResponseDto> findBySenderPhoneNumber(@PathVariable String senderPhoneNumber);

    List<TransferResponseDto> findByRecipientPhoneNumber(@PathVariable String recipientPhoneNumber);

    List<TransferResponseDto> findByStatus(@PathVariable TransactionStatus status);

    List<TransferResponseDto> findByTransactionType(@PathVariable TransactionType type);

    List<TransferResponseDto> findByUserPhoneNumber(@PathVariable String phoneNumber);

    CancelTransactionResponseDto cancelTransfer(@RequestBody CancelTransactionRequestDto cancelRequest);

    ResponseEntity<List<TransactionListDto>> getMyTransactions(@RequestParam(required = false) TransactionType type);
}
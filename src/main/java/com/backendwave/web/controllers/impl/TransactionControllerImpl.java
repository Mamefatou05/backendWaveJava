package com.backendwave.web.controllers.impl;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.services.TransactionService;
import com.backendwave.web.controllers.TransactionController;
import com.backendwave.web.dto.mappers.TransactionMapper;
import com.backendwave.web.dto.request.transactions.TransferRequestDto;
import com.backendwave.web.dto.response.transactions.TransferResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionControllerImpl(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @PostMapping("/transfer")
    @Override
    public TransferResponseDto transfer(@RequestBody TransferRequestDto transferRequestDto) {
        return transactionService.transfer(transferRequestDto);
    }

    @GetMapping("/sender/{senderPhoneNumber}")
    @Override
    public List<TransferResponseDto> findBySenderPhoneNumber(@PathVariable String senderPhoneNumber) {
        List<Transaction> transactions = transactionService.findBySenderPhoneNumber(senderPhoneNumber);
        return transactionMapper.toDtoList(transactions);
    }

    @GetMapping("/recipient/{recipientPhoneNumber}")
    @Override
    public List<TransferResponseDto> findByRecipientPhoneNumber(@PathVariable String recipientPhoneNumber) {
        List<Transaction> transactions = transactionService.findByRecipientPhoneNumber(recipientPhoneNumber);
        return transactionMapper.toDtoList(transactions);
    }

    @GetMapping("/status/{status}")
    @Override
    public List<TransferResponseDto> findByStatus(@PathVariable TransactionStatus status) {
        List<Transaction> transactions = transactionService.findByStatut(status);
        return transactionMapper.toDtoList(transactions);
    }

    @GetMapping("/planned/{dateTime}")
    @Override
    public List<TransferResponseDto> findPlannedTransactions(@PathVariable LocalDateTime dateTime) {
        List<Transaction> transactions = transactionService.findPlannedTransactions(dateTime);
        return transactionMapper.toDtoList(transactions);
    }

    @GetMapping("/type/{type}")
    @Override
    public List<TransferResponseDto> findByTransactionType(@PathVariable TransactionType type) {
        List<Transaction> transactions = transactionService.findByTypeTransaction(type);
        return transactionMapper.toDtoList(transactions);
    }

    @GetMapping("/user/{phoneNumber}")
    @Override
    public List<TransferResponseDto> findByUserPhoneNumber(@PathVariable String phoneNumber) {
        List<Transaction> transactions = transactionService.findByUserPhoneNumber(phoneNumber);
        return transactionMapper.toDtoList(transactions);
    }
}
package com.backendwave.web.controllers.impl;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.services.TransactionService;
import com.backendwave.web.controllers.TransactionController;
import com.backendwave.web.dto.mappers.TransactionMapper;
import com.backendwave.web.dto.request.transactions.CancelTransactionRequestDto;
import com.backendwave.web.dto.request.transactions.MultipleTransferRequestDto;
import com.backendwave.web.dto.request.transactions.TransferRequestDto;
import com.backendwave.web.dto.response.transactions.CancelTransactionResponseDto;
import com.backendwave.web.dto.response.transactions.TransferResponseDto;
import com.backendwave.web.dto.response.transactions.TransactionListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/transactions")
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionControllerImpl(TransactionService transactionService,
                                     TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    // Méthode utilitaire pour obtenir l'utilisateur connecté
    private Utilisateur getConnectedUser() {
        return (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/my-transactions")
    @Override
    public ResponseEntity<List<TransactionListDto>> getMyTransactions(
            @RequestParam(required = false) TransactionType type) {
        try {
            Utilisateur utilisateur = getConnectedUser();
            log.info("Récupération des transactions pour l'utilisateur {} avec filtre: {}",
                    utilisateur.getId(), type);

            List<TransactionListDto> transactions = transactionService.getUserTransactions(
                    utilisateur.getId(), type);

            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des transactions: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/transfer")
    @Override
    public TransferResponseDto transfer(@RequestBody TransferRequestDto transferRequestDto) {
        return transactionService.transfer(transferRequestDto);
    }

    @PostMapping("/transfer/multiple")
    @Override
    public ResponseEntity<List<TransferResponseDto>> multipleTransfer(@RequestBody MultipleTransferRequestDto requestDto) {
        try {
            log.info("Début du traitement de transfert multiple depuis {} vers {} destinataires",
                    requestDto.getSenderPhoneNumber(),
                    requestDto.getRecipientPhoneNumbers() != null ? requestDto.getRecipientPhoneNumbers().size() : 0);

            // Vérifier si l'utilisateur connecté est l'expéditeur
            Utilisateur connectedUser = getConnectedUser();
            if (!connectedUser.getNumeroTelephone().equals(requestDto.getSenderPhoneNumber())) {
                log.error("L'utilisateur connecté n'est pas l'expéditeur du transfert");
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(List.of());
            }

            List<TransferResponseDto> responses = transactionService.multipleTransfer(requestDto);

            log.info("Transfert multiple effectué avec succès");
            return ResponseEntity.ok(responses);

        } catch (IllegalArgumentException e) {
            log.error("Erreur de validation lors du transfert multiple: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(List.of());
        } catch (Exception e) {
            log.error("Erreur lors du transfert multiple: ", e);
            return ResponseEntity.internalServerError()
                    .body(List.of());
        }
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

    @PostMapping("/cancel")
    @Override
    public CancelTransactionResponseDto cancelTransfer(@RequestBody CancelTransactionRequestDto cancelRequest) {
        return transactionService.cancelTransfer(cancelRequest);
    }
}
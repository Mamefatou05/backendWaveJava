package com.backendwave.web.controllers.impl;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.backendwave.services.TransactionService;
import com.backendwave.web.controllers.TransactionController;
import com.backendwave.web.dto.mappers.TransactionMapper;
import com.backendwave.web.dto.request.transactions.CancelTransactionRequestDto;
import com.backendwave.web.dto.request.transactions.MultipleTransferRequestDto;
import com.backendwave.web.dto.request.transactions.TransferRequestDto;
import com.backendwave.web.dto.response.transactions.CancelTransactionResponseDto;
import com.backendwave.web.dto.response.transactions.TransferResponseDto;
import com.backendwave.web.dto.response.transactions.TransactionListDto;
import com.mfn.mydependance.exceptions.ResponseStatusException;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/transactions")
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public TransactionControllerImpl(TransactionService transactionService,
                                     TransactionMapper transactionMapper,
                                     UtilisateurRepository utilisateurRepository) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
        this.utilisateurRepository = utilisateurRepository;
    }

    private Utilisateur getConnectedUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.debug("Type de principal: {}", principal.getClass().getName());

            String userIdentifier;

            if (principal instanceof org.springframework.security.core.userdetails.User) {
                userIdentifier = ((org.springframework.security.core.userdetails.User) principal).getUsername();
            } else if (principal instanceof String) {
                userIdentifier = (String) principal;
            } else {
                userIdentifier = null;
            }

            log.debug("Identifiant utilisateur récupéré: {}", userIdentifier);

            // D'abord essayer avec le numéro de téléphone
            Utilisateur utilisateur = utilisateurRepository.findByNumeroTelephone(userIdentifier)
                    .orElse(null);

            // Si non trouvé, essayer avec l'email
            if (utilisateur == null) {
                utilisateur = utilisateurRepository.findByEmail(userIdentifier)
                        .orElseThrow(() -> new IllegalStateException(
                                "Utilisateur non trouvé avec l'identifiant: " + userIdentifier));
            }

            return utilisateur;

        } catch (Exception e) {
            log.error("Erreur lors de la récupération de l'utilisateur connecté: ", e);
            throw new IllegalStateException("Impossible de récupérer l'utilisateur connecté: " + e.getMessage());
        }
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
        } catch (IllegalStateException e) {
            log.error("Erreur d'authentification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des transactions: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/transfer")
    @Override
    public TransferResponseDto transfer(@RequestBody TransferRequestDto transferRequestDto) {
        try {
            // Vérifier si l'utilisateur connecté est l'expéditeur
            Utilisateur connectedUser = getConnectedUser();
            if (!connectedUser.getNumeroTelephone().equals(transferRequestDto.getSenderPhoneNumber())) {
                throw new IllegalStateException("L'utilisateur connecté n'est pas l'expéditeur du transfert");
            }
            return transactionService.transfer(transferRequestDto);
        } catch (IllegalStateException e) {
            log.error("Erreur d'autorisation: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/transfer/multiple")
    @Override
    public ResponseEntity<List<TransferResponseDto>> multipleTransfer(@RequestBody MultipleTransferRequestDto requestDto) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("Auth details - Principal: {}, Type: {}, Authorities: {}",
                    auth.getPrincipal(),
                    auth.getPrincipal().getClass().getName(),
                    auth.getAuthorities());

            log.info("Début du traitement de transfert multiple depuis {} vers {} destinataires",
                    requestDto.getSenderPhoneNumber(),
                    requestDto.getRecipientPhoneNumbers() != null ? requestDto.getRecipientPhoneNumbers().size() : 0);

            // Vérifier si l'utilisateur connecté est l'expéditeur
            Utilisateur connectedUser = getConnectedUser();
            if (!connectedUser.getNumeroTelephone().equals(requestDto.getSenderPhoneNumber())) {
                log.error("L'utilisateur connecté n'est pas l'expéditeur du transfert");
                throw new IllegalStateException("Vous n'êtes pas autorisé à effectuer ce transfert. " +
                        "L'expéditeur doit être l'utilisateur connecté.");
            }

            List<TransferResponseDto> responses = transactionService.multipleTransfer(requestDto);

            if (responses.isEmpty()) {
                log.warn("Aucun transfert n'a été effectué");
                throw new IllegalStateException("Aucun transfert n'a été effectué");
            }

            log.info("Transfert multiple effectué avec succès");
            return new ResponseEntity<>(responses, HttpStatus.OK);

        } catch (IllegalStateException e) {
            log.error("Erreur d'autorisation lors du transfert multiple: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Erreur de validation lors du transfert multiple: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Erreur lors du transfert multiple: ", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Une erreur inattendue s'est produite lors du traitement"
            );
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
        try {
            // Vérifier si l'utilisateur est autorisé à annuler la transaction
            Utilisateur connectedUser = getConnectedUser();
            // Vous pouvez ajouter ici une logique de vérification supplémentaire si nécessaire
            return transactionService.cancelTransfer(cancelRequest);
        } catch (IllegalStateException e) {
            log.error("Erreur d'autorisation lors de l'annulation: {}", e.getMessage());
            throw e;
        }
    }
}
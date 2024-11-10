// TransactionValidator.java
package com.backendwave.validators;

import com.backendwave.data.entities.Plafond;
import com.backendwave.data.entities.Transaction;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.web.dto.request.transactions.MultipleTransferRequestDto;
import com.backendwave.web.dto.request.transactions.TransferRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TransactionValidator {

    private static final Duration CANCELLATION_WINDOW = Duration.ofMinutes(30);
    private final PlafondValidator plafondValidator;

    public void validateTransferRequest(TransferRequestDto transferRequestDto) {
        if (transferRequestDto.getAmount() == null || transferRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du transfert doit être supérieur à zéro");
        }
        if (transferRequestDto.getSenderPhoneNumber().equals(transferRequestDto.getRecipientPhoneNumber())) {
            throw new IllegalArgumentException("L'expéditeur et le destinataire ne peuvent pas être identiques");
        }
    }

    public void validateSolde(BigDecimal soldeActuel, BigDecimal montant, BigDecimal frais) {
        BigDecimal totalAmount = montant.add(frais);
        if (soldeActuel == null || soldeActuel.compareTo(totalAmount) < 0) {
            String message = String.format(
                    "Solde insuffisant. Solde actuel: %,.2f FCFA, " +
                            "Montant demandé: %,.2f FCFA, " +
                            "Frais: %,.2f FCFA, " +
                            "Montant total nécessaire: %,.2f FCFA",
                    soldeActuel != null ? soldeActuel : BigDecimal.ZERO,
                    montant,
                    frais,
                    totalAmount
            );
            throw new IllegalArgumentException(message);
        }
    }

    public void validateCancellation(Transaction transaction) {
        // Vérifier le statut
        if (TransactionStatus.ANNULE.equals(transaction.getStatut())) {
            throw new IllegalStateException("Cette transaction a déjà été annulée");
        }

        // Vérifier le délai de 30 minutes
        LocalDateTime now = LocalDateTime.now();
        Duration timeSinceTransaction = Duration.between(transaction.getDateCreation(), now);

        if (timeSinceTransaction.compareTo(CANCELLATION_WINDOW) > 0) {
            throw new IllegalStateException(
                    String.format("Le délai d'annulation de 30 minutes est dépassé. " +
                                    "Temps écoulé depuis la transaction: %d minutes",
                            timeSinceTransaction.toMinutes())
            );
        }
    }

    public void validateCancellationBalance(Utilisateur recipient, BigDecimal transferAmount) {
        if (recipient.getSolde().compareTo(transferAmount) < 0) {
            throw new IllegalStateException(
                    "Le destinataire n'a pas suffisamment de fonds pour permettre l'annulation du transfert"
            );
        }
    }

    public void validateMultipleTransferRequest(MultipleTransferRequestDto requestDto) {
        // Vérification de la liste des destinataires
        if (requestDto.getRecipientPhoneNumbers() == null || requestDto.getRecipientPhoneNumbers().isEmpty()) {
            throw new IllegalArgumentException("La liste des destinataires ne peut pas être vide");
        }

        // Vérification du nombre minimum de destinataires
        if (requestDto.getRecipientPhoneNumbers().size() < 1) {
            throw new IllegalArgumentException("Il doit y avoir au moins un destinataire");
        }

        // Vérification du nombre maximum de destinataires
        if (requestDto.getRecipientPhoneNumbers().size() > 50) {
            throw new IllegalArgumentException("Le nombre maximum de destinataires est de 50");
        }

        // Validation du montant
        if (requestDto.getAmount() == null || requestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du transfert doit être supérieur à zéro");
        }

        // Validation du numéro de l'expéditeur
        if (requestDto.getSenderPhoneNumber() == null || requestDto.getSenderPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de l'expéditeur est obligatoire");
        }

        // Vérification que l'expéditeur n'est pas dans la liste des destinataires
        if (requestDto.getRecipientPhoneNumbers().contains(requestDto.getSenderPhoneNumber())) {
            throw new IllegalArgumentException("L'expéditeur ne peut pas être dans la liste des destinataires");
        }

        // Vérification des doublons dans la liste des destinataires
        Set<String> uniqueRecipients = new HashSet<>(requestDto.getRecipientPhoneNumbers());
        if (uniqueRecipients.size() != requestDto.getRecipientPhoneNumbers().size()) {
            throw new IllegalArgumentException("La liste des destinataires contient des doublons");
        }

        // Validation des numéros de téléphone des destinataires
        for (String recipientPhone : requestDto.getRecipientPhoneNumbers()) {
            if (recipientPhone == null || recipientPhone.trim().isEmpty()) {
                throw new IllegalArgumentException("Les numéros de téléphone des destinataires ne peuvent pas être vides");
            }

        }
    }
}
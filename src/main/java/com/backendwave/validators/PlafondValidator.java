// PlafondValidator.java
package com.backendwave.validators;

import com.backendwave.data.entities.Plafond;
import com.backendwave.data.entities.Transaction;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class PlafondValidator {

    private final TransactionRepository transactionRepository;

    public void verifierMontantMaxTransaction(BigDecimal montant, Plafond plafond) {
        if (montant.compareTo(plafond.getMontantMaxTransaction()) > 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "Le montant (%s) dépasse la limite maximale autorisée par transaction (%s)",
                            montant, plafond.getMontantMaxTransaction()
                    )
            );
        }
    }

    public void verifierLimiteJournaliere(BigDecimal montant, Utilisateur utilisateur, Plafond plafond) {
        LocalDateTime debutJour = LocalDate.now().atStartOfDay();
        LocalDateTime finJour = LocalDate.now().plusDays(1).atStartOfDay();

        BigDecimal totalJournalier = transactionRepository
                .findByExpediteur_IdAndDateCreationBetween(utilisateur.getId(), debutJour, finJour)
                .stream()
                .map(Transaction::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalJournalier.add(montant).compareTo(plafond.getLimiteJournaliere()) > 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "Le montant total des transactions journalières (%s) plus le montant actuel (%s) " +
                                    "dépasse la limite journalière autorisée de %s",
                            totalJournalier, montant, plafond.getLimiteJournaliere()
                    )
            );
        }
    }

    public void verifierLimiteMensuelle(BigDecimal montant, Utilisateur utilisateur, Plafond plafond) {
        YearMonth moisCourant = YearMonth.now();
        LocalDateTime debutMois = moisCourant.atDay(1).atStartOfDay();
        LocalDateTime finMois = moisCourant.atEndOfMonth().plusDays(1).atStartOfDay();

        BigDecimal totalMensuel = transactionRepository
                .findByExpediteur_IdAndDateCreationBetween(utilisateur.getId(), debutMois, finMois)
                .stream()
                .map(Transaction::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalMensuel.add(montant).compareTo(plafond.getLimiteMensuelle()) > 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "Le montant total des transactions mensuelles (%s) plus le montant actuel (%s) " +
                                    "dépasse la limite mensuelle autorisée de %s",
                            totalMensuel, montant, plafond.getLimiteMensuelle()
                    )
            );
        }
    }
}
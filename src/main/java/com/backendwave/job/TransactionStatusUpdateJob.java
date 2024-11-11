package com.backendwave.job;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionStatusUpdateJob {

    private final TransactionRepository transactionRepository;
    private static final int BATCH_SIZE = 30;
    private static final int MINUTES_DELAY = 30;

    @Scheduled(fixedDelay = 60000) 
    @Transactional
    public void updatePendingTransactions() {
        log.info("Début du job de mise à jour des transactions en attente");
        
        try {
            // Récupérer les transactions en attente créées il y a plus de 30 minutes
            LocalDateTime threshold = LocalDateTime.now().minusMinutes(MINUTES_DELAY);
            
            List<Transaction> pendingTransactions = transactionRepository
                .findByStatutAndDateCreationLessThanOrderByDateCreationAsc(
                    TransactionStatus.EN_ATTENTE,
                    threshold
                );

            // Traiter par lots de 30 transactions
            for (int i = 0; i < pendingTransactions.size(); i += BATCH_SIZE) {
                int endIndex = Math.min(i + BATCH_SIZE, pendingTransactions.size());
                List<Transaction> batch = pendingTransactions.subList(i, endIndex);
                
                updateBatch(batch);
                
                log.info("Lot de {} transactions mis à jour avec succès", batch.size());
            }

        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour des transactions en attente", e);
        }
    }

    private void updateBatch(List<Transaction> transactions) {
        transactions.forEach(transaction -> {
            transaction.setStatut(TransactionStatus.COMPLETE);
            log.debug("Mise à jour de la transaction {} : EN_ATTENTE -> COMPLETE", transaction.getId());
        });
        transactionRepository.saveAll(transactions);
    }
}
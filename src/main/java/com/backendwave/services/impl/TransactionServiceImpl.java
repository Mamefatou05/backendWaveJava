package com.backendwave.services.impl;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.entities.Plafond;
import com.backendwave.data.enums.Periodicity;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.data.repositories.TransactionRepository;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.backendwave.data.repositories.PlafondRepository;
import com.backendwave.services.TransactionService;
import com.backendwave.web.dto.request.transactions.CancelTransactionRequestDto;
import com.backendwave.web.dto.request.transactions.TransferRequestDto;
import com.backendwave.web.dto.response.transactions.CancelTransactionResponseDto;
import com.backendwave.web.dto.response.transactions.TransferResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PlafondRepository plafondRepository;

    private static final Duration CANCELLATION_WINDOW = Duration.ofMinutes(30);
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_SUPPORT = "SUPPORT";

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction non trouvée avec l'ID: " + id));
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public List<Transaction> findBySenderPhoneNumber(String senderPhoneNumber) {
        Utilisateur sender = utilisateurRepository.findByNumeroTelephone(senderPhoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Expéditeur introuvable"));
        return transactionRepository.findByExpediteur_Id(sender.getId());
    }

    @Override
    public List<Transaction> findByRecipientPhoneNumber(String recipientPhoneNumber) {
        Utilisateur recipient = utilisateurRepository.findByNumeroTelephone(recipientPhoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Destinataire introuvable"));
        return transactionRepository.findByDestinataire_Id(recipient.getId());
    }

    @Override
    public List<Transaction> findByStatut(TransactionStatus statut) {
        return transactionRepository.findByStatut(statut);
    }

    @Override
    public List<Transaction> findPlannedTransactions(LocalDateTime dateTime) {
        return transactionRepository.findByEstPlanifieTrueAndProchaineExecutionLessThanEqual(dateTime);
    }

    @Override
    public List<Transaction> findByTypeTransaction(TransactionType type) {
        return transactionRepository.findByTypeTransaction(type);
    }

    @Override
    public List<Transaction> findByUserPhoneNumber(String phoneNumber) {
        Utilisateur user = utilisateurRepository.findByNumeroTelephone(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        return transactionRepository.findByExpediteurOrDestinataire(user, user);
    }

    @Override
    @Transactional
    public TransferResponseDto transfer(TransferRequestDto transferRequestDto) {
        log.info("Début du traitement de transfert pour l'expéditeur: {}", transferRequestDto.getSenderPhoneNumber());

        // Validation des données de la requête
        validateTransferRequest(transferRequestDto);

        // Récupération des utilisateurs
        Utilisateur sender = utilisateurRepository.findByNumeroTelephone(transferRequestDto.getSenderPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("Expéditeur introuvable"));
        Utilisateur recipient = utilisateurRepository.findByNumeroTelephone(transferRequestDto.getRecipientPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("Destinataire introuvable"));

        // Calcul des frais
        BigDecimal transferFee = calculateTransferFee(transferRequestDto.getAmount());
        BigDecimal totalAmount = transferRequestDto.getAmount().add(transferFee);

        // Vérification du solde
        if (sender.getSolde() == null || sender.getSolde().compareTo(totalAmount) < 0) {
            String message = String.format(
                    "Solde insuffisant. Solde actuel: %,.2f FCFA, " +
                            "Montant demandé: %,.2f FCFA, " +
                            "Frais: %,.2f FCFA, " +
                            "Montant total nécessaire: %,.2f FCFA",
                    sender.getSolde() != null ? sender.getSolde() : BigDecimal.ZERO,
                    transferRequestDto.getAmount(),
                    transferFee,
                    totalAmount
            );
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        // Récupération et vérification des plafonds
        Plafond plafond = plafondRepository.findByUtilisateur_Id(sender.getId())
                .orElseThrow(() -> new IllegalArgumentException("Plafond non défini pour l'utilisateur"));

        verifierMontantMaxTransaction(transferRequestDto.getAmount(), plafond);
        verifierLimiteJournaliere(transferRequestDto.getAmount(), sender, plafond);
        verifierLimiteMensuelle(transferRequestDto.getAmount(), sender, plafond);

        // Création de la transaction
        Transaction transaction = createTransaction(transferRequestDto, sender, recipient, transferFee);

        // Mise à jour des soldes
        BigDecimal newSenderBalance = sender.getSolde().subtract(totalAmount);
        BigDecimal newRecipientBalance = recipient.getSolde().add(transferRequestDto.getAmount());

        sender.setSolde(newSenderBalance);
        recipient.setSolde(newRecipientBalance);

        // Sauvegarde des modifications
        utilisateurRepository.save(sender);
        utilisateurRepository.save(recipient);
        Transaction savedTransaction = transactionRepository.save(transaction);

        log.info("Transfert effectué avec succès, ID de transaction: {}", savedTransaction.getId());

        return createTransferResponse(savedTransaction, sender, recipient);
    }

    private void validateTransferRequest(TransferRequestDto transferRequestDto) {
        if (transferRequestDto.getAmount() == null || transferRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du transfert doit être supérieur à zéro");
        }
        if (transferRequestDto.getSenderPhoneNumber().equals(transferRequestDto.getRecipientPhoneNumber())) {
            throw new IllegalArgumentException("L'expéditeur et le destinataire ne peuvent pas être identiques");
        }
    }

    private BigDecimal calculateTransferFee(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Le montant ne peut pas être null");
        }
        return amount.multiply(new BigDecimal("0.01")).setScale(2, RoundingMode.HALF_UP);
    }

    private void verifierMontantMaxTransaction(BigDecimal montant, Plafond plafond) {
        if (montant.compareTo(plafond.getMontantMaxTransaction()) > 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "Le montant (%s) dépasse la limite maximale autorisée par transaction (%s)",
                            montant, plafond.getMontantMaxTransaction()
                    )
            );
        }
    }

    private void verifierLimiteJournaliere(BigDecimal montant, Utilisateur utilisateur, Plafond plafond) {
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

    private void verifierLimiteMensuelle(BigDecimal montant, Utilisateur utilisateur, Plafond plafond) {
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

    private Transaction createTransaction(TransferRequestDto transferRequestDto,
                                          Utilisateur sender, Utilisateur recipient, BigDecimal transferFee) {
        Transaction transaction = new Transaction();
        transaction.setExpediteur(sender);
        transaction.setDestinataire(recipient);
        transaction.setMontant(transferRequestDto.getAmount());
        transaction.setTypeTransaction(TransactionType.TRANSFERT);
        transaction.setStatut(TransactionStatus.EN_ATTENTE);
        transaction.setEstPlanifie(transferRequestDto.isPlanned());
        transaction.setPeriodicite(transferRequestDto.getPeriodicity());
        transaction.setReferenceGroupe(transferRequestDto.getGroupReference());
        transaction.setDateCreation(LocalDateTime.now());
        transaction.setFraisTransfert(transferFee);

        if (transaction.getEstPlanifie()) {
            transaction.setProchaineExecution(calculerProchaineExecution(transferRequestDto.getPeriodicity()));
        }

        return transaction;
    }

    private TransferResponseDto createTransferResponse(Transaction transaction, Utilisateur sender, Utilisateur recipient) {
        return TransferResponseDto.builder()
                .transactionId(transaction.getId())
                .status(transaction.getStatut())
                .transferFee(transaction.getFraisTransfert())
                .totalAmount(transaction.getMontant().add(transaction.getFraisTransfert()))
                .senderFullName(sender.getNomComplet())
                .receiverFullName(recipient.getNomComplet())
                .message("Transaction effectuée avec succès")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional
    public CancelTransactionResponseDto cancelTransfer(CancelTransactionRequestDto cancelRequest) {
        log.info("Tentative d'annulation du transfert avec l'ID: {}", cancelRequest.getTransactionId());

        // Récupérer la transaction à annuler
        Transaction transaction = transactionRepository.findById(cancelRequest.getTransactionId())
                .orElseThrow(() -> new IllegalArgumentException("Transaction non trouvée avec l'ID: " +
                        cancelRequest.getTransactionId()));

        // Vérifier si la transaction peut être annulée
        validateCancellation(transaction);

        // Récupérer l'expéditeur et le destinataire
        Utilisateur sender = transaction.getExpediteur();
        Utilisateur recipient = transaction.getDestinataire();

        // Calculer les montants à rembourser
        BigDecimal transferAmount = transaction.getMontant();
        BigDecimal transferFee = transaction.getFraisTransfert();
        BigDecimal totalAmount = transferAmount.add(transferFee);

        // Vérifier si le destinataire a suffisamment de fonds
        if (recipient.getSolde().compareTo(transferAmount) < 0) {
            throw new IllegalStateException(
                    "Le destinataire n'a pas suffisamment de fonds pour permettre l'annulation du transfert"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        // Mettre à jour la transaction originale
        transaction.setStatut(TransactionStatus.ANNULE);
        transaction.setDateAnnulation(now);
        transaction.setMotifAnnulation(cancelRequest.getMotifAnnulation());
        transactionRepository.save(transaction); // Sauvegarder la transaction annulée

        // Créer la transaction d'annulation
        Transaction cancellationTransaction = createCancellationTransaction(transaction, now);
        transactionRepository.save(cancellationTransaction); // Sauvegarder la transaction d'annulation

        // Mettre à jour les soldes
        sender.setSolde(sender.getSolde().add(totalAmount)); // Rembourser l'expéditeur
        recipient.setSolde(recipient.getSolde().subtract(transferAmount)); // Débiter le destinataire

        utilisateurRepository.save(sender);
        utilisateurRepository.save(recipient);

        log.info("Transfert annulé avec succès, ID de transaction: {}, ID transaction d'annulation: {}",
                transaction.getId(), cancellationTransaction.getId());

        return createCancellationResponse(transaction, cancellationTransaction, sender, recipient);
    }

    private void validateCancellation(Transaction transaction) {
        // Vérifier le statut
        if (TransactionStatus.ANNULE.equals(transaction.getStatut())) {
            throw new IllegalStateException("Cette transaction a déjà été annulée");
        }

        if (!TransactionStatus.COMPLETE.equals(transaction.getStatut())) {
            throw new IllegalStateException("Seules les transactions complétées peuvent être annulées");
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
    private Transaction createCancellationTransaction(Transaction originalTransaction, LocalDateTime now) {
        Transaction cancellationTransaction = new Transaction();
    
        cancellationTransaction.setExpediteur(originalTransaction.getDestinataire());
        cancellationTransaction.setDestinataire(originalTransaction.getExpediteur());
        cancellationTransaction.setMontant(originalTransaction.getMontant());
        // Correction ici : utilisation de ANNULE au lieu de ANNULATION
        cancellationTransaction.setTypeTransaction(TransactionType.ANNULE);
        cancellationTransaction.setStatut(TransactionStatus.COMPLETE);
        cancellationTransaction.setDateCreation(now);
        cancellationTransaction.setTransactionOrigine(originalTransaction);
        cancellationTransaction.setFraisTransfert(BigDecimal.ZERO);
        cancellationTransaction.setMotifAnnulation(originalTransaction.getMotifAnnulation());
    
        // Ajout de validation avant le retour
        if (cancellationTransaction.getTypeTransaction() == null) {
            throw new IllegalStateException("Le type de transaction ne peut pas être null");
        }
        
        // Validation des champs obligatoires
        if (cancellationTransaction.getExpediteur() == null || 
            cancellationTransaction.getDestinataire() == null ||
            cancellationTransaction.getMontant() == null) {
            throw new IllegalStateException("Les champs obligatoires ne peuvent pas être null");
        }
    
        log.debug("Création de la transaction d'annulation : type={}, montant={}, expéditeur={}, destinataire={}",
            cancellationTransaction.getTypeTransaction(),
            cancellationTransaction.getMontant(),
            cancellationTransaction.getExpediteur().getId(),
            cancellationTransaction.getDestinataire().getId());
    
        return cancellationTransaction;
    }

    private CancelTransactionResponseDto createCancellationResponse(
            Transaction originalTransaction,
            Transaction cancellationTransaction,
            Utilisateur sender,
            Utilisateur recipient) {

        return CancelTransactionResponseDto.builder()
                .transactionId(originalTransaction.getId())
                .cancellationTransactionId(cancellationTransaction.getId())
                .status(TransactionStatus.ANNULE)
                .montantRembourse(originalTransaction.getMontant())
                .fraisRembourses(originalTransaction.getFraisTransfert())
                .expediteur(sender.getNomComplet())
                .destinataire(recipient.getNomComplet())
                .motifAnnulation(originalTransaction.getMotifAnnulation())
                .dateAnnulation(originalTransaction.getDateAnnulation())
                .message("Transaction annulée avec succès")
                .build();
    }

    private LocalDateTime calculerProchaineExecution(Periodicity periodicity) {
        if (periodicity == null) return null;

        LocalDateTime now = LocalDateTime.now();
        return switch (periodicity) {
            case JOURNALIER -> now.plusDays(1);
            case HEBDOMADAIRE -> now.plusWeeks(1);
            case MENSUEL -> now.plusMonths(1);
        };
    }
}
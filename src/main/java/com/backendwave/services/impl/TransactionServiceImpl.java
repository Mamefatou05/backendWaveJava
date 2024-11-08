package com.backendwave.services.impl;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.entities.Plafond;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.data.repositories.TransactionRepository;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.backendwave.data.repositories.PlafondRepository;
import com.backendwave.services.TransactionService;
import com.backendwave.validators.TransactionValidator;
import com.backendwave.validators.PlafondValidator;
import com.backendwave.web.dto.request.transactions.CancelTransactionRequestDto;
import com.backendwave.web.dto.request.transactions.MultipleTransferRequestDto;
import com.backendwave.web.dto.request.transactions.TransferRequestDto;
import com.backendwave.web.dto.response.transactions.CancelTransactionResponseDto;
import com.backendwave.web.dto.response.transactions.TransactionListDto;
import com.backendwave.web.dto.response.transactions.TransferResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PlafondRepository plafondRepository;
    private final TransactionValidator transactionValidator;
    private final PlafondValidator plafondValidator;

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
        transactionValidator.validateTransferRequest(transferRequestDto);

        // Récupération des utilisateurs
        Utilisateur sender = utilisateurRepository.findByNumeroTelephone(transferRequestDto.getSenderPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("Expéditeur introuvable"));
        Utilisateur recipient = utilisateurRepository.findByNumeroTelephone(transferRequestDto.getRecipientPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("Destinataire introuvable"));

        // Calcul des frais
        BigDecimal transferFee = calculateTransferFee(transferRequestDto.getAmount());
        BigDecimal totalAmount = transferRequestDto.getAmount().add(transferFee);

        // Vérification du solde
        transactionValidator.validateSolde(sender.getSolde(), transferRequestDto.getAmount(), transferFee);

        // Récupération et vérification des plafonds
        Plafond plafond = plafondRepository.findByUtilisateur_Id(sender.getId())
                .orElseThrow(() -> new IllegalArgumentException("Plafond non défini pour l'utilisateur"));

        plafondValidator.verifierMontantMaxTransaction(transferRequestDto.getAmount(), plafond);
        plafondValidator.verifierLimiteJournaliere(transferRequestDto.getAmount(), sender, plafond);
        plafondValidator.verifierLimiteMensuelle(transferRequestDto.getAmount(), sender, plafond);

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

    private BigDecimal calculateTransferFee(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Le montant ne peut pas être null");
        }
        return amount.multiply(new BigDecimal("0.01")).setScale(2, RoundingMode.HALF_UP);
    }

    private Transaction createTransaction(TransferRequestDto transferRequestDto,
                                          Utilisateur sender, Utilisateur recipient, BigDecimal transferFee) {
        Transaction transaction = new Transaction();
        transaction.setExpediteur(sender);
        transaction.setDestinataire(recipient);
        transaction.setMontant(transferRequestDto.getAmount());
        transaction.setTypeTransaction(TransactionType.TRANSFERT);
        transaction.setStatut(TransactionStatus.EN_ATTENTE);
        transaction.setReferenceGroupe(transferRequestDto.getGroupReference());
        transaction.setDateCreation(LocalDateTime.now());
        transaction.setFraisTransfert(transferFee);
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
        transactionValidator.validateCancellation(transaction);

        // Récupérer l'expéditeur et le destinataire
        Utilisateur sender = transaction.getExpediteur();
        Utilisateur recipient = transaction.getDestinataire();

        // Calculer les montants à rembourser
        BigDecimal transferAmount = transaction.getMontant();
        BigDecimal transferFee = transaction.getFraisTransfert();
        BigDecimal totalAmount = transferAmount.add(transferFee);

        // Vérifier si le destinataire a suffisamment de fonds
        transactionValidator.validateCancellationBalance(recipient, transferAmount);

        LocalDateTime now = LocalDateTime.now();

        // Mettre à jour la transaction originale
        transaction.setStatut(TransactionStatus.ANNULE);
        transaction.setDateAnnulation(now);
        transaction.setMotifAnnulation(cancelRequest.getMotifAnnulation());
        transactionRepository.save(transaction);

        // Créer la transaction d'annulation
        Transaction cancellationTransaction = createCancellationTransaction(transaction, now);
        transactionRepository.save(cancellationTransaction);

        // Mettre à jour les soldes
        sender.setSolde(sender.getSolde().add(totalAmount));
        recipient.setSolde(recipient.getSolde().subtract(transferAmount));

        utilisateurRepository.save(sender);
        utilisateurRepository.save(recipient);

        log.info("Transfert annulé avec succès, ID de transaction: {}, ID transaction d'annulation: {}",
                transaction.getId(), cancellationTransaction.getId());

        return createCancellationResponse(transaction, cancellationTransaction, sender, recipient);
    }

    private Transaction createCancellationTransaction(Transaction originalTransaction, LocalDateTime now) {
        Transaction cancellationTransaction = new Transaction();

        cancellationTransaction.setExpediteur(originalTransaction.getDestinataire());
        cancellationTransaction.setDestinataire(originalTransaction.getExpediteur());
        cancellationTransaction.setMontant(originalTransaction.getMontant());
        cancellationTransaction.setTypeTransaction(TransactionType.ANNULE);
        cancellationTransaction.setStatut(TransactionStatus.COMPLETE);
        cancellationTransaction.setDateCreation(now);
        cancellationTransaction.setTransactionOrigine(originalTransaction);
        cancellationTransaction.setFraisTransfert(BigDecimal.ZERO);
        cancellationTransaction.setMotifAnnulation(originalTransaction.getMotifAnnulation());

        // Validation des champs obligatoires
        if (cancellationTransaction.getExpediteur() == null ||
                cancellationTransaction.getDestinataire() == null ||
                cancellationTransaction.getMontant() == null ||
                cancellationTransaction.getTypeTransaction() == null) {
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

    @Override
    public List<TransactionListDto> getUserTransactions(Long userId, TransactionType type) {
        log.info("Récupération des transactions pour l'utilisateur {} avec filtre: {}", userId, type);

        List<Transaction> transactions = transactionRepository.findUserTransactionsWithFilter(userId, type);

        return transactions.stream()
                .map(transaction -> convertToListDto(transaction, userId))
                .collect(Collectors.toList());
    }

    private TransactionListDto convertToListDto(Transaction transaction, Long connectedUserId) {
        boolean estEmetteur = transaction.getExpediteur().getId().equals(connectedUserId);
        String autrePartiePrenante;

        if (transaction.getTypeTransaction() == TransactionType.TRANSFERT) {
            // Pour les transferts, on montre l'autre partie (expéditeur ou destinataire)
            autrePartiePrenante = estEmetteur ?
                    transaction.getDestinataire().getNomComplet() :
                    transaction.getExpediteur().getNomComplet();
        } else {
            // Pour les autres types (DEPOT, RETRAIT, etc.)
            autrePartiePrenante = "Wave"; // ou le nom de votre service
        }

        return TransactionListDto.builder()
                .id(transaction.getId())
                .montant(transaction.getMontant())
                .typeTransaction(transaction.getTypeTransaction())
                .statut(transaction.getStatut())
                .dateCreation(transaction.getDateCreation())
                .autrePartiePrenante(autrePartiePrenante)
                .estEmetteur(estEmetteur)
                .build();
    }

    @Transactional
    @Override
    public List<TransferResponseDto> multipleTransfer(MultipleTransferRequestDto requestDto) {
        log.info("Début du traitement de transfert multiple depuis {} vers {} destinataires",
                requestDto.getSenderPhoneNumber(), requestDto.getRecipientPhoneNumbers().size());

        transactionValidator.validateMultipleTransferRequest(requestDto);

        // Générer une référence de groupe si elle n'existe pas
        String groupReference = requestDto.getGroupReference() != null ?
                requestDto.getGroupReference() :
                "MULTI_" + System.currentTimeMillis();

        // Récupérer l'expéditeur avec verrou pessimiste
        Utilisateur sender = utilisateurRepository.findByNumeroTelephoneForUpdate(requestDto.getSenderPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("Expéditeur introuvable"));

        // Calculer le montant total nécessaire
        BigDecimal transferFeePerTransaction = calculateTransferFee(requestDto.getAmount());
        BigDecimal totalFees = transferFeePerTransaction.multiply(
                new BigDecimal(requestDto.getRecipientPhoneNumbers().size()));
        BigDecimal totalAmount = requestDto.getAmount()
                .multiply(new BigDecimal(requestDto.getRecipientPhoneNumbers().size()))
                .add(totalFees);

        // Vérification initiale du solde total
        if (sender.getSolde().compareTo(totalAmount) < 0) {
            throw new IllegalArgumentException(String.format(
                    "Solde insuffisant pour effectuer tous les transferts. Nécessaire: %s, Disponible: %s",
                    totalAmount, sender.getSolde()));
        }

        // Vérification des plafonds sur le montant total
        Plafond plafond = plafondRepository.findByUtilisateur_Id(sender.getId())
                .orElseThrow(() -> new IllegalArgumentException("Plafond non défini pour l'utilisateur"));

        plafondValidator.verifierMontantMaxTransaction(totalAmount, plafond);
        plafondValidator.verifierLimiteJournaliere(totalAmount, sender, plafond);
        plafondValidator.verifierLimiteMensuelle(totalAmount, sender, plafond);

        List<TransferResponseDto> responses = new ArrayList<>();
        List<String> failedRecipients = new ArrayList<>();

        // Traiter chaque destinataire
        for (String recipientPhone : requestDto.getRecipientPhoneNumbers()) {
            try {
                TransferRequestDto individualTransfer = TransferRequestDto.builder()
                        .senderPhoneNumber(requestDto.getSenderPhoneNumber())
                        .recipientPhoneNumber(recipientPhone)
                        .amount(requestDto.getAmount())
                        .groupReference(groupReference)  // Ajout de la référence de groupe
                        .build();

                TransferResponseDto response = transfer(individualTransfer);
                responses.add(response);

                log.info("Transfert réussi vers {}, ID transaction: {}",
                        recipientPhone, response.getTransactionId());

            } catch (Exception e) {
                log.error("Échec du transfert vers {}: {}", recipientPhone, e.getMessage());
                failedRecipients.add(recipientPhone);
            }
        }

        // En cas d'échec partiel, on log mais on continue
        if (!failedRecipients.isEmpty()) {
            log.warn("Certains transferts ont échoué vers: {}", String.join(", ", failedRecipients));
        }

        // Vérifier qu'au moins un transfert a réussi
        if (responses.isEmpty()) {
            throw new RuntimeException("Aucun transfert n'a réussi");
        }

        return responses;
    }
}
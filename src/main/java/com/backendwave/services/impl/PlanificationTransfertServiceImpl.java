package com.backendwave.services.impl;

import com.backendwave.data.entities.Notification;
import com.backendwave.data.entities.PlanificationTransfert;
import com.backendwave.data.entities.Transaction;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.enums.Periodicity;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.data.repositories.PlanificationTransfertRepository;
import com.backendwave.data.repositories.TransactionRepository;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.backendwave.services.NotificationService;
import com.backendwave.services.PlanificationTransfertService;
import com.backendwave.web.dto.request.transactions.PlanificationTransfertDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
public class PlanificationTransfertServiceImpl implements PlanificationTransfertService {


    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;
    private final UtilisateurRepository utilisateurRepository;
    private final PlanificationTransfertRepository planificationRepository;

    private PlanificationTransfertServiceImpl(TransactionRepository transactionRepository, PlanificationTransfertRepository planificationRep, NotificationService notificationService , UtilisateurRepository utilisateurRepository) {
        this.transactionRepository = transactionRepository;
        this.planificationRepository = planificationRep;
        this.notificationService = notificationService;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public void traiterPlanification(PlanificationTransfert planification) {
        System.out.println("azerty dans le traitement de la planification");
        BigDecimal montantTotal = planification.getMontant();  // Montant initial

        if (planification.getExpediteur().getSolde().compareTo(montantTotal) >= 0) {
            // Déduire le montant total du solde de l'expéditeur
            planification.getExpediteur().setSolde(planification.getExpediteur().getSolde().subtract(montantTotal));

            // Effectuer la transaction avec le montant net (après déduction des frais)
            effectuerTransaction(planification, TransactionStatus.COMPLETE);

            // Calculer la prochaine date d'exécution
            planification.setProchaineExecution(calculateNextExecution(planification));
        } else {
            // Si les fonds sont insuffisants, annuler la transaction
            effectuerTransaction(planification, TransactionStatus.ANNULE);
            notifyUser(planification.getExpediteur(), "Votre transaction programmée n'a pas pu être effectuée en raison de fonds insuffisants.");
        }

        planificationRepository.save(planification);
    }


    @Override
    public void relancerPlanification(Long planificationId) {

        PlanificationTransfert planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new IllegalArgumentException("Planification non trouvée"));
        traiterPlanification(planification);
    }


    @Override
    public void annulerPlanification(Long planificationId) {
        PlanificationTransfert planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new IllegalArgumentException("Planification non trouvée"));
        planification.setProchaineExecution(null);  // Marque comme annulée
        planificationRepository.save(planification);
    }


    private void effectuerTransaction(PlanificationTransfert planification, TransactionStatus statut) {
        System.out.println("azertyuio dans effectuer transaction");

        // Calcul des frais et du montant net
        BigDecimal frais = calculerFrais(planification.getMontant());
        BigDecimal montantNet = planification.getMontant().subtract(frais);

        Transaction transaction = new Transaction();
        transaction.setExpediteur(planification.getExpediteur());
        transaction.setDestinataire(planification.getDestinataire());
        transaction.setMontant(montantNet);  // Montant après déduction des frais
        transaction.setFraisTransfert(frais); // Frais de transfert
        transaction.setTypeTransaction(TransactionType.TRANSFERT);
        transaction.setStatut(statut);
        System.out.printf(String.valueOf(transaction));

        // Sauvegarde de la transaction
        transactionRepository.save(transaction);
    }

    private LocalDateTime calculateNextExecution(PlanificationTransfert planification) {
        // Calculer la prochaine date d'exécution en fonction de la périodicité
        switch (planification.getPeriodicite()) {
            case JOURNALIER:
                return planification.getProchaineExecution().plusDays(1);
            case HEBDOMADAIRE:
                return planification.getProchaineExecution().plusWeeks(1);
            case MENSUEL:
                return planification.getProchaineExecution().plusMonths(1);
            default:
                return planification.getProchaineExecution();
        }
    }
    @Override
    public void desactiverPlanification(Long planificationId) {
        PlanificationTransfert planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new IllegalArgumentException("Planification non trouvée"));
        planification.setEstActif(false);  // Désactiver la planification
        planificationRepository.save(planification);
    }
    private void activerPlanification(Long planificationId) {
        PlanificationTransfert planification = planificationRepository.findById(planificationId)
               .orElseThrow(() -> new IllegalArgumentException("Planification non trouvée"));
        planification.setEstActif(true);  // Activer la planification
        planificationRepository.save(planification);
    }

    private void notifyUser(Utilisateur utilisateur, String message) {
        Notification notification = new Notification();
        notification.setTitre("Planification annulée");
        notification.setMessage(message);
        notification.setUtilisateur(utilisateur);
        notification.setTypeNotification(TransactionType.ANNULE);
        notificationService.sendNotification(notification, utilisateur);
    }
    private BigDecimal calculerFrais(BigDecimal montant) {
        BigDecimal pourcentageFrais = new BigDecimal("0.01"); // 2% de frais
        return montant.multiply(pourcentageFrais).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    @Override
    public PlanificationTransfert creerPlanification(PlanificationTransfertDTO planificationDTO) {
        // Récupérer les utilisateurs par numéro de téléphone
        Utilisateur expediteur = utilisateurRepository.findByNumeroTelephone(planificationDTO.getExpediteurTelephone())
                .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé avec le numéro de téléphone: " + planificationDTO.getExpediteurTelephone()));

        Utilisateur destinataire = utilisateurRepository.findByNumeroTelephone(planificationDTO.getDestinataireTelephone())
                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé avec le numéro de téléphone: " + planificationDTO.getDestinataireTelephone()));

        // Créer l'entité PlanificationTransfert à partir du DTO
        PlanificationTransfert planification = new PlanificationTransfert();
        planification.setExpediteur(expediteur);
        planification.setDestinataire(destinataire);
        planification.setMontant(planificationDTO.getMontant());
        planification.setPeriodicite(planificationDTO.getPeriodicite());
        planification.setReferenceGroupe(planificationDTO.getReferenceGroupe());
        planification.setHeureExecution(planificationDTO.getHeureExecution());

        // Calculer la prochaine exécution
        planification.setProchaineExecution(calculateExecution(planification));

        // Enregistrer la planification
        return planificationRepository.save(planification);
    }

    private LocalDateTime calculateExecution(PlanificationTransfert planification) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextExecution = now;

        // Ajouter la date en fonction de la périodicité
        switch (planification.getPeriodicite()) {
            case JOURNALIER:
                nextExecution = now.plusDays(1);
                break;
            case HEBDOMADAIRE:
                nextExecution = now.plusWeeks(1);
                break;
            case MENSUEL:
                nextExecution = now.plusMonths(1);
                break;
            default:
                nextExecution = now;
                break;
        }

        // Ajouter l'heure d'exécution spécifique
        nextExecution = nextExecution.withHour(planification.getHeureExecution().getHour())
                .withMinute(planification.getHeureExecution().getMinute())
                .withSecond(0)
                .withNano(0);

        return nextExecution;
    }



}

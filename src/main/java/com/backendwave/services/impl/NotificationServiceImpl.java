package com.backendwave.services.impl;

import com.backendwave.data.entities.Notification;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.enums.NotificationType;
import com.backendwave.data.repositories.NotificationRepository;
import com.backendwave.services.EmailService;
import com.backendwave.services.NotificationService;
import com.backendwave.services.SmsService;
import com.backendwave.services.WhatsappService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final SmsService smsService;
    private final WhatsappService whatsappService;

    @Autowired
    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            EmailService emailService,
            SmsService smsService,
            WhatsappService whatsappService) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
        this.smsService = smsService;
        this.whatsappService = whatsappService;
    }

    // Enregistrer une notification
    public Notification create(Notification notification, Utilisateur utilisateur) {
        notification.setUtilisateur(utilisateur);
        return notificationRepository.save(notification);
    }

    // Envoyer une notification en fonction du type choisi par l'utilisateur
    public void sendNotification(Notification notification, Utilisateur utilisateur) {
        NotificationType notificationType = utilisateur.getTypeNotification(); // On suppose qu'il y a un attribut type de notification
        switch (notificationType) {
            case SMS:
                smsService.sendSms(utilisateur.getNumeroTelephone(), notification.getMessage());
                break;
            case EMAIL:
                emailService.sendEmail(utilisateur.getEmail(), notification.getTitre(), notification.getMessage());
                break;
            case WHATSAPP:
                whatsappService.sendWhatsapp(utilisateur.getNumeroTelephone(), notification.getMessage());
                break;
            default:
                throw new UnsupportedOperationException("Type de notification non supporté");
        }
        // Enregistrer la notification en BD
        create(notification, utilisateur);
    }

    // Obtenir toutes les notifications d'un utilisateur
    public List<Notification> getNotificationsByUtilisateur(Long utilisateurId) {
        return notificationRepository.findByUtilisateur_Id(utilisateurId);
    }

    // Obtenir les notifications non lues d'un utilisateur
    public List<Notification> getUnreadNotifications(Long utilisateurId) {
        return notificationRepository.findByUtilisateurIdAndEstLueFalse(utilisateurId);
    }

    // Compter les notifications non lues d'un utilisateur
    public long countUnreadNotifications(Long utilisateurId) {
        return notificationRepository.countByUtilisateurIdAndEstLueFalse(utilisateurId);
    }
}

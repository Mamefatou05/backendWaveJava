package com.backendwave.web.controllers.impl;


import com.backendwave.data.entities.Notification;
import com.backendwave.services.NotificationService;
import com.backendwave.web.controllers.NotificationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationControllerImpl implements NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationControllerImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Obtenir toutes les notifications d'un utilisateur
    @GetMapping("/utilisateur/{id}")
    public List<Notification> getNotificationsByUtilisateur(@PathVariable("id") Long utilisateurId) {
        return notificationService.getNotificationsByUtilisateur(utilisateurId);
    }

    // Obtenir les notifications non lues d'un utilisateur
    @GetMapping("/utilisateur/{id}/unread")
    public List<Notification> getUnreadNotifications(@PathVariable("id") Long utilisateurId) {
        return notificationService.getUnreadNotifications(utilisateurId);
    }

    // Compter les notifications non lues d'un utilisateur
    @GetMapping("/utilisateur/{id}/unread/count")
    public long countUnreadNotifications(@PathVariable("id") Long utilisateurId) {
        return notificationService.countUnreadNotifications(utilisateurId);
    }
}

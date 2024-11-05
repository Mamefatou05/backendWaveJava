package com.backendwave.services;

import com.backendwave.data.entities.Notification;
import com.backendwave.data.entities.Utilisateur;

import java.util.List;

public interface NotificationService {
    public long countUnreadNotifications(Long utilisateurId) ;
    public List<Notification> getUnreadNotifications(Long utilisateurId);
    public List<Notification> getNotificationsByUtilisateur(Long utilisateurId);
    public void sendNotification(Notification notification, Utilisateur utilisateur);
    public Notification create(Notification notification, Utilisateur utilisateur);

}

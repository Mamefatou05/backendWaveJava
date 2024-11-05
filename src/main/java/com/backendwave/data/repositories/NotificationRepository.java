package com.backendwave.data.repositories;

import com.backendwave.data.entities.Notification;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends BaseRepository<Notification> {
    
    // Trouver toutes les notifications d'un utilisateur
    List<Notification> findByUtilisateur_Id(Long utilisateurId);
    
    // Trouver les notifications non lues d'un utilisateur
    List<Notification> findByUtilisateurIdAndEstLueFalse(Long utilisateurId);
    
    // Compter les notifications non lues d'un utilisateur
    long countByUtilisateurIdAndEstLueFalse(Long utilisateurId);
} 
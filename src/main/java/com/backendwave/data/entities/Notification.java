package com.backendwave.data.entities;

import com.backendwave.data.enums.NotificationType;
import jakarta.persistence.*;

@Entity
public class Notification extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Column(nullable = false, length = 100)
    private String titre;

    @Lob
    private String message;

    private Boolean estLue = false;

    @Enumerated(EnumType.STRING)
    private NotificationType typeNotification;

    // Getters and setters
}

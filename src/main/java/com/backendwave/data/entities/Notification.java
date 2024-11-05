package com.backendwave.data.entities;

import com.backendwave.data.enums.NotificationType;
import com.backendwave.data.enums.TransactionType;
import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
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
    private TransactionType typeNotification;

    // Getters and setters
}

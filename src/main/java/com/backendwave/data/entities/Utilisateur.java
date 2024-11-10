package com.backendwave.data.entities;

import com.backendwave.data.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur extends BaseEntity {

    @Column(nullable = false, unique = true, length = 15)
    private String numeroTelephone;

    @Column(nullable = false, length = 100)
    private String nomComplet;

    @Column(unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String codeQr;

    @Column(precision = 15, scale = 2)
    private BigDecimal solde = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Role role;

    private Boolean estActif = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType typeNotification = NotificationType.EMAIL;

    // Relations
    @OneToMany(mappedBy = "utilisateur")
    @JsonManagedReference

    private List<Notification> notifications;

    @OneToMany(mappedBy = "expediteur")
    @JsonManagedReference

    private List<Transaction> transactionsEnvoyees;

    @OneToMany(mappedBy = "destinataire")
    @JsonManagedReference

    private List<Transaction> transactionsRecues;

    @OneToMany(mappedBy = "client")
    @JsonManagedReference
    private List<NumeroFavori> numerosFavoris;

    @OneToMany(mappedBy = "utilisateur")
    @JsonManagedReference

    private List<Plafond> plafonds;

    @OneToMany(mappedBy = "expediteur")
    @JsonManagedReference

    private List<PlanificationTransfert> planificationsEnvoyees;

    @OneToMany(mappedBy = "destinataire")
    @JsonManagedReference

    private List<PlanificationTransfert> planificationsRecues;

    @OneToOne(mappedBy = "utilisateur")
    @JsonManagedReference

    private Marchand marchand;

    @OneToOne(mappedBy = "agent")
    @JsonManagedReference

    private PointAgent pointAgent;
}

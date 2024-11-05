package com.backendwave.data.entities;

import com.backendwave.data.enums.Periodicity;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Transaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "expediteur_id")
    private Utilisateur expediteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private Utilisateur destinataire;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType typeTransaction;

    @Enumerated(EnumType.STRING)
    private TransactionStatus statut = TransactionStatus.EN_ATTENTE;

    private Boolean estPlanifie = false;

    @Enumerated(EnumType.STRING)
    private Periodicity periodicite;

    private LocalDateTime prochaineExecution;

    @Column(length = 50)
    private String referenceGroupe;

    @Column(precision = 7, scale = 2)
    private BigDecimal fraisTransfert;

}

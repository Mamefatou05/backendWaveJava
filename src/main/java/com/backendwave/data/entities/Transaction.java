package com.backendwave.data.entities;

import com.backendwave.data.enums.Periodicity;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Transaction extends BaseEntity {

    @ManyToOne
    private Utilisateur expediteur;

    @ManyToOne
    private Utilisateur destinataire;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType typeTransaction;

//    private Boolean estPlanifie = false;

    @Enumerated(EnumType.STRING)
    private TransactionStatus statut = TransactionStatus.EN_ATTENTE;


    @Column(length = 50)
    private String referenceGroupe;

    @Column(precision = 7, scale = 2)
    private BigDecimal fraisTransfert;

    @ManyToOne
    @JoinColumn(name = "transaction_origine_id")
    private Transaction transactionOrigine;

    private LocalDateTime dateAnnulation;

    @Column(length = 255)
    private String motifAnnulation;
}
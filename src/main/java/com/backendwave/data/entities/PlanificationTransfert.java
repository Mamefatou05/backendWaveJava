package com.backendwave.data.entities;

import com.backendwave.data.enums.Periodicity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
public class PlanificationTransfert extends BaseEntity {

    @ManyToOne

    @JoinColumn(nullable = false)
    @JsonBackReference
    private Utilisateur expediteur;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonBackReference
    private Utilisateur destinataire;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Periodicity periodicite;

    @Column(nullable = false)
    private LocalDateTime prochaineExecution;

    private Boolean estActif = true;

    private String referenceGroupe;
    private LocalTime heureExecution;  // Heure d'exécution de la planification


}

package com.backendwave.data.entities;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Plafond extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal limiteJournaliere;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal limiteMensuelle;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montantMaxTransaction;


}

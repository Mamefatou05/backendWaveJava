package com.backendwave.data.entities;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
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

package com.backendwave.data.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class CodesVerification extends BaseEntity {

    @Column( nullable = false)
    private Long utilisateurId; // Référence à l'entité Utilisateur

    @Column( nullable = false)
    private String code;

    @Column( nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column( nullable = false)
    private LocalDateTime dateExpiration;

    @Column( nullable = false)
    private boolean estUtilise;
}

package com.backendwave.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Marchand extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String nomCommercial;

    @Column(nullable = false)
    private String adresse;

    private boolean estActif = true;

    @OneToOne
    private Utilisateur utilisateur;
}
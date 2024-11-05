package com.backendwave.data.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Utilisateur extends BaseEntity {

    @Column(nullable = false, unique = true, length = 15)
    private String numeroTelephone;

    @Column(nullable = false, length = 100)
    private String nomComplet;

    @Column(unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String motDePasseHash;

    @Lob
    private String codeQr;

    @Column(precision = 15, scale = 2)
    private BigDecimal solde = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Role role;

    private Boolean estActif = false;

}

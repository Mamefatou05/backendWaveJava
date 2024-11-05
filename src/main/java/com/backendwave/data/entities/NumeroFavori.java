package com.backendwave.data.entities;


import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class NumeroFavori extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Utilisateur client;

    @Column(nullable = false, length = 15)
    private String numeroTelephone;

    @Column(length = 100)
    private String nom;

    @Column(updatable = false)
    private LocalDateTime dateAjout = LocalDateTime.now();

}

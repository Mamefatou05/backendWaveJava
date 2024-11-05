package com.backendwave.data.entities;

import com.backendwave.data.enums.NotificationType;
import jakarta.persistence.*;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
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

    private NotificationType notificationType = NotificationType.EMAIL;


}

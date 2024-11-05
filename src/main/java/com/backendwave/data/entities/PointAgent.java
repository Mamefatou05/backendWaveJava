package com.backendwave.data.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class PointAgent extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private Utilisateur agent;

    @Column(nullable = false, length = 255)
    private String adresse;

    private Boolean estActif = true;

    @Column(precision = 15, scale = 2)
    private BigDecimal soldeFlottant = BigDecimal.ZERO;

}

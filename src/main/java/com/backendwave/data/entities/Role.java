package com.backendwave.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Role extends BaseEntity {
    @Column(nullable = false, length = 50)
    private String nom;

}

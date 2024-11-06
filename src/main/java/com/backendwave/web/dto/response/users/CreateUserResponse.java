package com.backendwave.web.dto.response.users;

import lombok.Data;

@Data
public class CreateUserResponse {

    private Long id; // ID de l'utilisateur créé
    private String numeroTelephone;
    private String nomComplet;
    private String email;
    private String codeQr;
    private Boolean estActif;
    private String typeNotification;

    public CreateUserResponse(Long id, String numeroTelephone, String nomComplet, String email, String codeQr, Boolean estActif, String typeNotification) {
        this.id = id;
        this.numeroTelephone = numeroTelephone;
        this.nomComplet = nomComplet;
        this.email = email;
        this.codeQr = codeQr;
        this.estActif = estActif;
        this.typeNotification = typeNotification;
    }
}
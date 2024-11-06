package com.backendwave.web.dto.request.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank
    @Size(max = 15)
    private String numeroTelephone;

    @NotBlank
    @Size(max = 100)
    private String nomComplet;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 6, max = 255)
    private String password;

    private String codeQr; // Optionnel

    private Long roleId; // ID du rôle, à utiliser pour la liaison avec l'entité Role

    private Boolean estActif = false;

    public CreateUserRequest() {
        // Constructeur par défaut
    }
}

    

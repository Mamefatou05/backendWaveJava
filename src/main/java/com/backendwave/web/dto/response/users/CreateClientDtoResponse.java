package com.backendwave.web.dto.response.users;

import com.backendwave.data.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientDtoResponse {

   private Long id;
   
   private String numeroTelephone;
   
   private String nomComplet;
   
   private String email;
   
   private String codeQr;
   
   private BigDecimal solde;
   
   private Boolean estActif;
   
   private NotificationType typeNotification;
   
   private String roleNom;  // Juste le nom du rôle au lieu de tout l'objet Role
}
package com.backendwave.web.dto.request.users;

import com.backendwave.data.enums.NotificationType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientDto {
   
   @NotBlank(message = "Le nom complet est obligatoire")
   @Size(min = 2, max = 100, message = "Le nom complet doit contenir entre 2 et 100 caractères") 
   private String nomComplet;

   @NotBlank(message = "Le numéro de téléphone est obligatoire")
   @Pattern(regexp = "^\\+?[0-9]{9,14}$", message = "Le numéro de téléphone doit être valide")
   private String numeroTelephone;

   @NotBlank(message = "L'adresse email est obligatoire")
   private String email;

   @NotBlank(message = "Le code PIN est obligatoire")
   @Pattern(
       regexp = "^(?!.*(\\d)\\1)(?!0123|1234|2345|3456|4567|5678|6789|9876|8765|7654|6543|5432|4321|3210)\\d{4}$",
       message = "Le code PIN doit contenir exactement 4 chiffres non successifs et non répétitifs"
   )
   private String password;

   @NotBlank(message = "La confirmation du code PIN est obligatoire")
   private String confirmPassword;

}
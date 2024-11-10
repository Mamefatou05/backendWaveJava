package com.backendwave.web.dto.request.transactions;

import com.backendwave.data.enums.Periodicity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO pour les requêtes de transfert d'argent
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDto {

    /**
     * Numéro de téléphone de l'expéditeur
     */
    @NotNull(message = "Le numéro de téléphone de l'expéditeur ne peut pas être null")
    @Size(min = 9, max = 15, message = "Le numéro de téléphone de l'expéditeur doit être entre 9 et 15 caractères")
    private String senderPhoneNumber;

    /**
     * Numéro de téléphone du destinataire
     */
    @NotNull(message = "Le numéro de téléphone du destinataire ne peut pas être null")
    @Size(min = 9, max = 15, message = "Le numéro de téléphone du destinataire doit être entre 9 et 15 caractères")
    private String recipientPhoneNumber;

    /**
     * Montant à transférer
     */
    @NotNull(message = "Le montant ne peut pas être null")
    @Positive(message = "Le montant doit être supérieur à zéro")
    private BigDecimal amount;

    /**
     * Périodicité du transfert (optionnel)
     * Peut être JOURNALIER, HEBDOMADAIRE ou MENSUEL
     */
    private Periodicity periodicity;

    /**
     * Référence de groupe pour les transferts liés (optionnel)
     */
    @Size(max = 50, message = "La référence de groupe ne doit pas dépasser 50 caractères")
    private String groupReference;

    /**
     * Vérifie si le transfert est planifié
     * @return true si une périodicité est définie
     */
    public boolean isPlanned() {
        return periodicity != null;
    }
}
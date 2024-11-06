package com.backendwave.web.dto.request.transactions;

import java.math.BigDecimal;

import com.backendwave.data.enums.Periodicity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TransferRequestDto {
    @NotNull(message = "Le numéro de téléphone de l'expéditeur ne peut pas être nul.")
    @Size(min = 9, max = 20, message = "Le numéro de téléphone de l'expéditeur doit être entre 10 et 20 caractères.")
    private String senderPhoneNumber;

    @NotNull(message = "Le numéro de téléphone du destinataire ne peut pas être nul.")
    @Size(min = 9, max = 20, message = "Le numéro de téléphone du destinataire doit être entre 10 et 20 caractères.")
    private String recipientPhoneNumber;

    @NotNull(message = "Le montant ne peut pas être nul.")
    @Positive(message = "Le montant doit être positif.")
    private BigDecimal amount;

    private Periodicity periodicity;

    @Size(max = 50)
    private String groupReference;
}
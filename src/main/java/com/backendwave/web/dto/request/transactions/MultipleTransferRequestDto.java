package com.backendwave.web.dto.request.transactions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class MultipleTransferRequestDto {
    @NotBlank(message = "Le numéro de l'expéditeur est obligatoire")
    private String senderPhoneNumber;

    @NotEmpty(message = "La liste des destinataires ne peut pas être vide")
    private List<String> recipientPhoneNumbers;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private BigDecimal amount;

    private String groupReference;
}
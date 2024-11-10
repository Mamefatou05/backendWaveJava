package com.backendwave.web.dto.request.transactions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CancelTransactionRequestDto {
    @NotNull(message = "L'ID de la transaction est obligatoire")
    private Long transactionId;

    @NotBlank(message = "Le motif d'annulation est obligatoire")
    private String motifAnnulation;
}
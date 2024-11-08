package com.backendwave.web.dto.response.transactions;

import com.backendwave.data.enums.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CancelTransactionResponseDto {
    private Long transactionId;
    private Long cancellationTransactionId;
    private TransactionStatus status;
    private BigDecimal montantRembourse;
    private BigDecimal fraisRembourses;
    private String expediteur;
    private String destinataire;
    private String motifAnnulation;
    private LocalDateTime dateAnnulation;
    private String message;
}
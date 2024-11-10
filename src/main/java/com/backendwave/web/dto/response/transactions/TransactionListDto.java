package com.backendwave.web.dto.response.transactions;

import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionListDto {
    private Long id;
    private BigDecimal montant;
    private TransactionType typeTransaction;
    private TransactionStatus statut;
    private LocalDateTime dateCreation;
    private String autrePartiePrenante;
    private boolean estEmetteur;
}
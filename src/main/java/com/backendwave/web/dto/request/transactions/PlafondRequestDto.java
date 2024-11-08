package com.backendwave.web.dto.request.transactions;

import java.math.BigDecimal;

public class PlafondRequestDto {
    private BigDecimal limiteJournaliere;
    private BigDecimal limiteMensuelle;
    private BigDecimal montantMaxTransaction;

    // Getters et Setters
    public BigDecimal getLimiteJournaliere() {
        return limiteJournaliere;
    }

    public void setLimiteJournaliere(BigDecimal limiteJournaliere) {
        this.limiteJournaliere = limiteJournaliere;
    }

    public BigDecimal getLimiteMensuelle() {
        return limiteMensuelle;
    }

    public void setLimiteMensuelle(BigDecimal limiteMensuelle) {
        this.limiteMensuelle = limiteMensuelle;
    }

    public BigDecimal getMontantMaxTransaction() {
        return montantMaxTransaction;
    }

    public void setMontantMaxTransaction(BigDecimal montantMaxTransaction) {
        this.montantMaxTransaction = montantMaxTransaction;
    }
}

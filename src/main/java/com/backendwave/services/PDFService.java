package com.backendwave.services;

import java.math.BigDecimal;

/**
 * Interface pour la génération des documents PDF
 */
public interface PDFService {

    /**
     * Génère un PDF de bienvenue pour un nouveau client
     *
     * @param nomComplet Le nom complet du client
     * @param qrCode Le code QR du client en format Base64
     * @param limiteJournaliere La limite journalière des transactions
     * @param limiteMensuelle La limite mensuelle des transactions
     * @param montantMaxTransaction Le montant maximum par transaction
     * @return Un tableau de bytes contenant le PDF généré
     * @throws RuntimeException Si une erreur survient pendant la génération du PDF
     */
    byte[] generateWelcomePDF(
            String nomComplet,
            String qrCode,
            BigDecimal limiteJournaliere,
            BigDecimal limiteMensuelle,
            BigDecimal montantMaxTransaction
    );

    /**
     * Formate un montant en ajoutant les séparateurs de milliers
     *
     * @param montant Le montant à formater
     * @return Le montant formaté sous forme de chaîne de caractères
     */
    String formatMontant(BigDecimal montant);
}
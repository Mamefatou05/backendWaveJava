package com.backendwave.services.impl;

import com.backendwave.services.PDFService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;

@Service
public class PDFServiceImpl implements PDFService {

    @Override
    public byte[] generateWelcomePDF(String nomComplet, String qrCode, BigDecimal limiteJournaliere,
                                     BigDecimal limiteMensuelle, BigDecimal montantMaxTransaction) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Ajout du logo (à adapter selon votre logo)
            // Image logo = Image.getInstance("chemin/vers/votre/logo.png");
            // document.add(logo);

            // Titre
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.BLUE);
            Paragraph title = new Paragraph("Bienvenue sur notre plateforme!", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Informations du client
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            document.add(new Paragraph("Cher(e) " + nomComplet + ",", normalFont));
            document.add(new Paragraph("Nous sommes ravis de vous compter parmi nos utilisateurs!", normalFont));
            document.add(new Paragraph("\n"));

            // Limites de transaction
            Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            document.add(new Paragraph("Vos limites de transaction:", subTitleFont));

            document.add(new Paragraph("• Limite journalière: " + formatMontant(limiteJournaliere) + " FCFA", normalFont));
            document.add(new Paragraph("• Limite mensuelle: " + formatMontant(limiteMensuelle) + " FCFA", normalFont));
            document.add(new Paragraph("• Montant maximum par transaction: " + formatMontant(montantMaxTransaction) + " FCFA", normalFont));
            document.add(new Paragraph("\n"));

            // QR Code
            if (qrCode != null && !qrCode.isEmpty()) {
                document.add(new Paragraph("Votre QR Code:", subTitleFont));
                byte[] qrCodeBytes = Base64.getDecoder().decode(qrCode.replace("data:image/png;base64,", ""));
                Image qrCodeImage = Image.getInstance(qrCodeBytes);
                qrCodeImage.scaleToFit(100, 100);
                qrCodeImage.setAlignment(Element.ALIGN_CENTER);
                document.add(qrCodeImage);
            }

            // Pied de page
            document.add(new Paragraph("\n"));
            Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            document.add(new Paragraph("Pour toute assistance, n'hésitez pas à nous contacter.", footerFont));

            document.close();
            return outputStream.toByteArray();

        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Erreur lors de la génération du PDF: " + e.getMessage());
        }
    }

    @Override
    public String formatMontant(BigDecimal montant) {
        return String.format("%,d", montant.longValue());
    }
}
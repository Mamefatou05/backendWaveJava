package com.backendwave.services.impl;

import com.backendwave.services.PDFService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;

@Service
public class PDFServiceImpl implements PDFService {

    // Couleurs
    private static final BaseColor PRIMARY_COLOR = new BaseColor(63, 81, 181);    // Bleu indigo
    private static final BaseColor SECONDARY_COLOR = new BaseColor(33, 33, 33);   // Noir profond
    private static final BaseColor ACCENT_COLOR = new BaseColor(240, 244, 248);   // Gris clair
    private static final BaseColor BORDER_COLOR = new BaseColor(207, 216, 220);   // Gris bordure
    private static final BaseColor WAVE_BLUE = new BaseColor(0, 153, 255);        // Bleu Wave
    private static final BaseColor SUCCESS_GREEN = new BaseColor(76, 175, 80);    // Vert
    private static final BaseColor SHADOW_COLOR = new BaseColor(238, 238, 238);   // Gris ombre

    // Icônes Unicode
    private static final String WAVE_ICON = "🌊";
    private static final String DAILY_ICON = "⌚";
    private static final String MONTHLY_ICON = "📅";
    private static final String MAX_ICON = "💰";
    private static final String QR_ICON = "📱";
    private static final String SCAN_ICON = "📲";
    private static final String EMAIL_ICON = "📧";
    private static final String PHONE_ICON = "📞";
    private static final String INFO_ICON = "ℹ️";

    @Override
    public byte[] generateWelcomePDF(String nomComplet, String qrCode, BigDecimal limiteJournaliere,
                                   BigDecimal limiteMensuelle, BigDecimal montantMaxTransaction) {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            addHeader(document, nomComplet);
            addLimitsSection(document, limiteJournaliere, limiteMensuelle, montantMaxTransaction);
            
            if (qrCode != null && !qrCode.isEmpty()) {
                addQRCodeSection(document, qrCode);
            }
            
            addFooter(document);

            document.close();
            return outputStream.toByteArray();

        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Erreur lors de la génération du PDF: " + e.getMessage());
        }
    }

    private void addHeader(Document document, String nomComplet) throws DocumentException {
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        header.setSpacingAfter(30);

        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(WAVE_BLUE);
        headerCell.setPadding(40);
        headerCell.setBorder(Rectangle.NO_BORDER);

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 32, Font.BOLD, BaseColor.WHITE);
        Paragraph title = new Paragraph(WAVE_ICON + " WAVE", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        
        Font welcomeFont = new Font(Font.FontFamily.HELVETICA, 24, Font.NORMAL, BaseColor.WHITE);
        Paragraph welcome = new Paragraph("Bienvenue " + nomComplet + " !", welcomeFont);
        welcome.setAlignment(Element.ALIGN_CENTER);
        welcome.setSpacingBefore(20);

        headerCell.addElement(title);
        headerCell.addElement(welcome);
        header.addCell(headerCell);

        document.add(header);
    }

    private void addLimitsSection(Document document, BigDecimal limiteJournaliere,
                                BigDecimal limiteMensuelle, BigDecimal montantMaxTransaction) 
                                throws DocumentException {
        PdfPTable limitsCard = new PdfPTable(2);
        limitsCard.setWidthPercentage(100);
        limitsCard.setWidths(new float[]{1, 4});
        limitsCard.setSpacingAfter(30);

        // Titre de la section
        Font sectionFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, PRIMARY_COLOR);
        Paragraph sectionTitle = new Paragraph(INFO_ICON + " Vos Limites de Transaction", sectionFont);
        
        PdfPCell titleCell = new PdfPCell(sectionTitle);
        titleCell.setColspan(2);
        titleCell.setPadding(20);
        titleCell.setBackgroundColor(ACCENT_COLOR);
        titleCell.setBorder(Rectangle.NO_BORDER);
        limitsCard.addCell(titleCell);

        // Limites
        addLimitRow(limitsCard, DAILY_ICON, "Limite journalière", formatMontant(limiteJournaliere) + " FCFA");
        addLimitRow(limitsCard, MONTHLY_ICON, "Limite mensuelle", formatMontant(limiteMensuelle) + " FCFA");
        addLimitRow(limitsCard, MAX_ICON, "Maximum par transaction", formatMontant(montantMaxTransaction) + " FCFA");

        PdfPTable cardWrapper = new PdfPTable(1);
        cardWrapper.setWidthPercentage(100);
        PdfPCell wrapperCell = new PdfPCell(limitsCard);
        wrapperCell.setBorderColor(BORDER_COLOR);
        wrapperCell.setBorderWidth(1);
        wrapperCell.setPadding(0);
        wrapperCell.setCellEvent(new CellBackground());
        
        cardWrapper.addCell(wrapperCell);
        document.add(cardWrapper);
    }

    private void addLimitRow(PdfPTable table, String icon, String label, String value) {
        Font iconFont = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, PRIMARY_COLOR);
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, SECONDARY_COLOR);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, PRIMARY_COLOR);
        
        PdfPCell iconCell = new PdfPCell(new Phrase(icon, iconFont));
        iconCell.setBorder(Rectangle.NO_BORDER);
        iconCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        iconCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        iconCell.setPadding(10);
        
        Paragraph content = new Paragraph();
        content.add(new Chunk(label + "\n", labelFont));
        content.add(new Chunk(value, valueFont));
        
        PdfPCell contentCell = new PdfPCell(content);
        contentCell.setBorder(Rectangle.NO_BORDER);
        contentCell.setPadding(10);
        
        table.addCell(iconCell);
        table.addCell(contentCell);
    }

    private void addQRCodeSection(Document document, String qrCode) throws DocumentException, IOException {
        PdfPTable qrCard = new PdfPTable(1);
        qrCard.setWidthPercentage(70);
        qrCard.setHorizontalAlignment(Element.ALIGN_CENTER);
        qrCard.setSpacingAfter(30);

        // Titre QR Code
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, PRIMARY_COLOR);
        Paragraph qrTitle = new Paragraph();
        qrTitle.add(new Chunk(QR_ICON + " Votre Code QR Personnel", titleFont));
        qrTitle.setAlignment(Element.ALIGN_CENTER);
        
        PdfPCell titleCell = new PdfPCell(qrTitle);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setPadding(20);
        titleCell.setBackgroundColor(ACCENT_COLOR);
        titleCell.setBorder(Rectangle.NO_BORDER);
        qrCard.addCell(titleCell);

        // QR Code Image
        byte[] qrCodeBytes = Base64.getDecoder().decode(qrCode.replace("data:image/png;base64,", ""));
        Image qrCodeImage = Image.getInstance(qrCodeBytes);
        qrCodeImage.scaleToFit(250, 250);

        PdfPCell qrCell = new PdfPCell(qrCodeImage);
        qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        qrCell.setPadding(40);
        qrCell.setBackgroundColor(BaseColor.WHITE);
        qrCell.setBorder(Rectangle.NO_BORDER);
        qrCard.addCell(qrCell);

        // Instructions
        Font instructionFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC, SECONDARY_COLOR);
        Paragraph instruction = new Paragraph();
        instruction.add(new Chunk(SCAN_ICON + " Scannez ce code pour effectuer rapidement des transactions", instructionFont));
        instruction.setAlignment(Element.ALIGN_CENTER);
        
        PdfPCell instructionCell = new PdfPCell(instruction);
        instructionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        instructionCell.setPadding(20);
        instructionCell.setBorder(Rectangle.NO_BORDER);
        qrCard.addCell(instructionCell);

        // Wrapper
        PdfPTable cardWrapper = new PdfPTable(1);
        cardWrapper.setWidthPercentage(100);
        PdfPCell wrapperCell = new PdfPCell(qrCard);
        wrapperCell.setBorderColor(BORDER_COLOR);
        wrapperCell.setBorderWidth(1);
        wrapperCell.setPadding(0);
        wrapperCell.setCellEvent(new QRCodeBackground());
        
        cardWrapper.addCell(wrapperCell);
        document.add(cardWrapper);
    }

    private void addFooter(Document document) throws DocumentException {
        PdfPTable footer = new PdfPTable(1);
        footer.setWidthPercentage(100);

        // Contact info
        Font contactFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, SECONDARY_COLOR);
        Paragraph contacts = new Paragraph();
        contacts.add(new Chunk(EMAIL_ICON + " support@wave.com    "));
        contacts.add(new Chunk(PHONE_ICON + " +221 XX XXX XX XX"));
        contacts.setAlignment(Element.ALIGN_CENTER);
        
        PdfPCell contactCell = new PdfPCell(contacts);
        contactCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        contactCell.setPadding(20);
        contactCell.setBorder(Rectangle.NO_BORDER);
        footer.addCell(contactCell);

        // Copyright
        Font copyrightFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, SECONDARY_COLOR);
        Paragraph copyright = new Paragraph("© 2024 Wave. Tous droits réservés.", copyrightFont);
        copyright.setAlignment(Element.ALIGN_CENTER);
        
        PdfPCell copyrightCell = new PdfPCell(copyright);
        copyrightCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        copyrightCell.setPadding(20);
        copyrightCell.setBorder(Rectangle.TOP);
        copyrightCell.setBorderColor(BORDER_COLOR);
        footer.addCell(copyrightCell);

        document.add(footer);
    }

    private static class CellBackground implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.BACKGROUNDCANVAS];
            cb.setRGBColorFill(238, 238, 238);
            cb.roundRectangle(
                rect.getLeft() + 2, rect.getBottom() - 2,
                rect.getWidth() - 4, rect.getHeight() - 4, 8
            );
            cb.fill();
        }
    }

    private static class QRCodeBackground implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.BACKGROUNDCANVAS];
            PdfShading shading = PdfShading.simpleAxial(
                canvas[PdfPTable.BACKGROUNDCANVAS].getPdfWriter(),
                rect.getLeft(), rect.getBottom(),
                rect.getRight(), rect.getTop(),
                new BaseColor(255, 255, 255),
                new BaseColor(240, 244, 248)
            );
            cb.paintShading(shading);
        }
    }

    @Override
    public String formatMontant(BigDecimal montant) {
        return String.format("%,d", montant.longValue());
    }
}
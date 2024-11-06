package com.backendwave.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class QRCodeGenerator {

    @Value("${frontend.url}")
    private String frontendUrl;

    public String generateQRCodeBase64(String telephone) {
        try {
            // Créer l'URL complète avec le numéro de téléphone en paramètre
            String completeUrl = String.format("%s/verify/%s", frontendUrl, telephone);

            // Configurer le générateur de QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    completeUrl,  // Utiliser l'URL complète comme contenu
                    BarcodeFormat.QR_CODE,
                    250,  // largeur
                    250   // hauteur
            );

            // Convertir le QR code en image PNG puis en Base64
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la génération du QR code", e);
        }
    }
}
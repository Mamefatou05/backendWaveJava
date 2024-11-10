package com.backendwave.services.impl;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QRCodeValidationService {

    private final UtilisateurRepository utilisateurRepository;

    // Cette méthode décode le QR code et valide le numéro de téléphone
    public Utilisateur validateQRCode(String qrCodeBase64) {
        try {
            // Convertir la chaîne Base64 en image
            byte[] imageBytes = java.util.Base64.getDecoder().decode(qrCodeBase64);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);

            // Créer une source de luminance pour l'image
            BufferedImageLuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);

            // Créer un objet BinaryBitmap à partir de la source de luminance
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));

            // Décoder le QR code avec ZXing
            MultiFormatReader multiFormatReader = new MultiFormatReader();
            Result result = multiFormatReader.decode(binaryBitmap);

            // Récupérer le texte décodé et vérifier si le numéro de téléphone existe
            String decodedData = result.getText();
            // Vérifier si le numéro de téléphone existe et retourner l'utilisateur
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByNumeroTelephone(decodedData);
            return utilisateurOpt.orElse(null); // Retourne l'utilisateur ou null si non trouvé
        } catch (Exception e) {
            // En cas d'erreur de décodage, retourner false
            return null;
        }
    }
}
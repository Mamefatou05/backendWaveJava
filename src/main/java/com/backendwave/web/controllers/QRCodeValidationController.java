package com.backendwave.web.controllers;

import com.backendwave.data.entities.Utilisateur;
import com.backendwave.services.impl.QRCodeValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qrcode")
@RequiredArgsConstructor
public class QRCodeValidationController {

    private final QRCodeValidationService qrCodeValidationService;

    @PostMapping("/validate")
    public ResponseEntity<?> validateQRCode(@RequestBody String qrCodeBase64) {
        Utilisateur user = qrCodeValidationService.validateQRCode(qrCodeBase64);


        if (user != null) {
            // Si l'utilisateur est trouvé, renvoyez-le dans la réponse
            return ResponseEntity.ok(user);
        } else {
            // Si l'utilisateur n'est pas trouvé ou si le QR code est invalide
            return ResponseEntity.status(400).body("QR code invalide ou utilisateur non trouvé.");
        }
    }
}

package com.backendwave.data.seeders;

import com.backendwave.data.entities.Marchand;
import com.backendwave.data.repositories.MarchandRepository;
import com.backendwave.data.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarchandSeeder {

    private final MarchandRepository marchandRepository;
    private final UtilisateurRepository utilisateurRepository;

    public void seed() {
        if (marchandRepository.count() == 0) {
            // Récupérer les premiers utilisateurs clients pour créer leurs marchands
            utilisateurRepository.findByEmail("client1@example.com").ifPresent(utilisateur -> {
                Marchand marchand1 = new Marchand();
                marchand1.setNomCommercial("Wave Shop");
                marchand1.setAdresse("Colobane, Dakar");
                marchand1.setEstActif(true);
                marchand1.setUtilisateur(utilisateur);
                marchandRepository.save(marchand1);
            });

            utilisateurRepository.findByEmail("client2@example.com").ifPresent(utilisateur -> {
                Marchand marchand2 = new Marchand();
                marchand2.setNomCommercial("Dakar Market");
                marchand2.setAdresse("Médina, Dakar");
                marchand2.setEstActif(true);
                marchand2.setUtilisateur(utilisateur);
                marchandRepository.save(marchand2);
            });

            utilisateurRepository.findByEmail("client3@example.com").ifPresent(utilisateur -> {
                Marchand marchand3 = new Marchand();
                marchand3.setNomCommercial("Senegal Store");
                marchand3.setAdresse("Plateau, Dakar");
                marchand3.setEstActif(true);
                marchand3.setUtilisateur(utilisateur);
                marchandRepository.save(marchand3);
            });
        }
    }
}
package com.backendwave.data.seeders;

import com.backendwave.data.entities.Plafond;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.repositories.PlafondRepository;
import com.backendwave.data.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PlafondSeeder {

    private final PlafondRepository plafondRepository;
    private final UtilisateurRepository utilisateurRepository;

    public void seed() {
        if (plafondRepository.count() == 0) {
            utilisateurRepository.findAll().forEach(utilisateur -> {
                createPlafond(
                        utilisateur,
                        new BigDecimal("2000000"), // 2 millions par jour
                        new BigDecimal("10000000"), // 10 millions par mois
                        new BigDecimal("1000000")  // 1 million par transaction
                );
            });
        }
    }

    private void createPlafond(Utilisateur utilisateur, BigDecimal limiteJour,
                               BigDecimal limiteMois, BigDecimal limiteTransaction) {
        Plafond plafond = new Plafond();
        plafond.setUtilisateur(utilisateur);
        plafond.setLimiteJournaliere(limiteJour);
        plafond.setLimiteMensuelle(limiteMois);
        plafond.setMontantMaxTransaction(limiteTransaction);
        plafondRepository.save(plafond);
    }
} 
package com.backendwave.services.impl;

import com.backendwave.data.entities.Plafond;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.repositories.PlafondRepository;
import com.backendwave.services.PlafondService;
import com.backendwave.web.dto.request.transactions.PlafondRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class PlafondServiceImpl implements PlafondService {

    private final PlafondRepository plafondRepository;


    public Plafond createDefaultPlafond(Utilisateur utilisateur) {
        Plafond plafond = new Plafond();
        plafond.setUtilisateur(utilisateur);

        // Définir les limites par défaut
        plafond.setLimiteJournaliere(new BigDecimal("1000000")); // 1 million par jour
        plafond.setLimiteMensuelle(new BigDecimal("20000000")); // 20 millions par mois
        plafond.setMontantMaxTransaction(new BigDecimal("500000")); // 500 mille par transaction

        return plafondRepository.save(plafond);
    }

    // @Override
    // public Optional<Plafond> findByUtilisateurId(Long utilisateurId) {
    //     return plafondRepository.findByUtilisateur_Id(utilisateurId);
    // }

    @Autowired
    public PlafondServiceImpl(PlafondRepository plafondRepository) {
        this.plafondRepository = plafondRepository;
    }

    @Override
    public Optional<Plafond> getPlafondByUtilisateurId(Long utilisateurId) {
        return plafondRepository.findByUtilisateur_Id(utilisateurId);
    }

    @Override
    public Plafond modifierPlafond(Long utilisateurId, PlafondRequestDto plafondRequestDto) {
        Optional<Plafond> plafondOpt = plafondRepository.findByUtilisateur_Id(utilisateurId);
        if (plafondOpt.isEmpty()) {
            throw new IllegalArgumentException("Plafond non trouvé pour l'utilisateur avec ID : " + utilisateurId);
        }

        Plafond plafond = plafondOpt.get();

        // Logique de validation - exemple de valeurs maximales
//        BigDecimal LIMITE_JOURNALIERE_MAX = new BigDecimal("10000");
//        BigDecimal LIMITE_MENSUELLE_MAX = new BigDecimal("50000");
//        BigDecimal MONTANT_MAX_TRANSACTION = new BigDecimal("1000");
//
//        if (plafondRequestDto.getLimiteJournaliere().compareTo(LIMITE_JOURNALIERE_MAX) > 0) {
//            throw new IllegalArgumentException("La limite journalière dépasse la limite maximale autorisée.");
//        }
//        if (plafondRequestDto.getLimiteMensuelle().compareTo(LIMITE_MENSUELLE_MAX) > 0) {
//            throw new IllegalArgumentException("La limite mensuelle dépasse la limite maximale autorisée.");
//        }
//        if (plafondRequestDto.getMontantMaxTransaction().compareTo(MONTANT_MAX_TRANSACTION) > 0) {
//            throw new IllegalArgumentException("Le montant maximal de transaction dépasse la limite autorisée.");
//        }

        plafond.setLimiteJournaliere(plafondRequestDto.getLimiteJournaliere());
        plafond.setLimiteMensuelle(plafondRequestDto.getLimiteMensuelle());
        plafond.setMontantMaxTransaction(plafondRequestDto.getMontantMaxTransaction());

        return plafondRepository.save(plafond);
    }
}

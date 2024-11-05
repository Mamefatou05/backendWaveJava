package com.backendwave.data.seeders;

import com.backendwave.data.entities.PointAgent;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.repositories.PointAgentRepository;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.backendwave.data.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PointAgentSeeder {

    private final PointAgentRepository pointAgentRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;

    public void seed() {
        if (pointAgentRepository.count() == 0) {
            utilisateurRepository.findByRole_Id(
                roleRepository.findByNom("AGENT").orElseThrow().getId()
            ).forEach(agent -> {
                createPointAgent(
                    agent,
                    "Rue 123, Quartier Commerce, Abidjan",
                    true,
                    new BigDecimal("5000000") // 5 millions de solde flottant initial
                );
            });
        }
    }

    private void createPointAgent(Utilisateur agent, String adresse, 
                                Boolean estActif, BigDecimal soldeFlottant) {
        PointAgent pointAgent = new PointAgent();
        pointAgent.setAgent(agent);
        pointAgent.setAdresse(adresse);
        pointAgent.setEstActif(estActif);
        pointAgent.setSoldeFlottant(soldeFlottant);
        pointAgentRepository.save(pointAgent);
    }
} 
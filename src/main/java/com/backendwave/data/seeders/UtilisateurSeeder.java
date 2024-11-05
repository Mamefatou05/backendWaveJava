package com.backendwave.data.seeders;

import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.entities.Role;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.backendwave.data.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class UtilisateurSeeder {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void seed() {
        if (utilisateurRepository.count() == 0) {
            Role roleAdmin = roleRepository.findByNom("ADMIN").orElseThrow();
            Role roleAgent = roleRepository.findByNom("AGENT").orElseThrow();

            // Création admin
            createUtilisateur(
                "+22501020304",
                "Admin System",
                "admin@wave.com",
                "password123",
                roleAdmin,
                true
            );

            // Création agent
            createUtilisateur(
                "+22505060708",
                "Agent Principal",
                "agent@wave.com",
                "password123",
                roleAgent,
                true
            );
        }
    }

    private void createUtilisateur(String telephone, String nom, String email, 
                                 String password, Role role, Boolean estActif) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNumeroTelephone(telephone);
        utilisateur.setNomComplet(nom);
        utilisateur.setEmail(email);
        utilisateur.setMotDePasseHash(passwordEncoder.encode(password));
        utilisateur.setRole(role);
        utilisateur.setEstActif(estActif);
        utilisateur.setSolde(BigDecimal.ZERO);
        utilisateurRepository.save(utilisateur);
    }
} 
package com.backendwave.data.seeders;

import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.entities.Role;
import com.backendwave.data.enums.NotificationType;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.backendwave.data.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class UtilisateurSeeder {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void seed() {
        if (utilisateurRepository.count() == 0) {
            Role roleClient = roleRepository.findByNom("CLIENT").orElseThrow(() -> new RuntimeException("Role CLIENT non trouvé"));
            Role roleAdmin = roleRepository.findByNom("ADMIN").orElseThrow(() -> new RuntimeException("Role ADMIN non trouvé"));
            Role roleAgent = roleRepository.findByNom("AGENT").orElseThrow(() -> new RuntimeException("Role AGENT non trouvé"));
            
            // Create 2 Admins
            for (int i = 1; i <= 2; i++) {
                Utilisateur admin = new Utilisateur();
                admin.setNumeroTelephone(String.format("+22176%07d", i));
                admin.setNomComplet("Admin " + i);
                admin.setEmail("admin" + i + "@wave.com");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setSolde(BigDecimal.ZERO);
                admin.setRole(roleAdmin);
                admin.setEstActif(true);
                admin.setTypeNotification(NotificationType.EMAIL);
                utilisateurRepository.save(admin);
            }
            
            // Create 10 Clients
            for (int i = 1; i <= 10; i++) {
                Utilisateur client = new Utilisateur();
                client.setNumeroTelephone(String.format("+22177%07d", i));
                client.setNomComplet("Client " + i);
                client.setEmail("client" + i + "@example.com");
                client.setPassword(passwordEncoder.encode("Client@123"));
                client.setSolde(new BigDecimal("10000"));
                client.setRole(roleClient);
                client.setEstActif(true);
                client.setTypeNotification(NotificationType.SMS);
                utilisateurRepository.save(client);
            }
            
            // Create 3 Agents
            for (int i = 1; i <= 3; i++) {
                Utilisateur agent = new Utilisateur();
                agent.setNumeroTelephone(String.format("+22178%07d", i));
                agent.setNomComplet("Agent " + i);
                agent.setEmail("agent" + i + "@wave.com");
                agent.setPassword(passwordEncoder.encode("Agent@123"));
                agent.setSolde(new BigDecimal("1000000"));
                agent.setRole(roleAgent);
                agent.setEstActif(true);
                agent.setTypeNotification(NotificationType.WHATSAPP);
                utilisateurRepository.save(agent);
            }
        }
    }
}
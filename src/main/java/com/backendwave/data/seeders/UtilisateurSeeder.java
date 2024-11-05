package com.backendwave.data.seeders;

import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.entities.Role;
import com.backendwave.data.enums.NotificationType;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.backendwave.data.repositories.RoleRepository;
import com.backendwave.utils.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class UtilisateurSeeder {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    public void seed() {
        if (utilisateurRepository.count() == 0) {
            try {
                Role roleClient = roleRepository.findByNom("CLIENT").orElseThrow(() -> new RuntimeException("Role CLIENT non trouvé"));
                Role roleAdmin = roleRepository.findByNom("ADMIN").orElseThrow(() -> new RuntimeException("Role ADMIN non trouvé"));
                Role roleAgent = roleRepository.findByNom("AGENT").orElseThrow(() -> new RuntimeException("Role AGENT non trouvé"));
                
                // Create 2 Admins
                for (int i = 1; i <= 2; i++) {
                    Utilisateur admin = new Utilisateur();
                    String telephone = String.format("+22176%07d", i);
                    admin.setNumeroTelephone(telephone);
                    admin.setNomComplet("Admin " + i);
                    admin.setEmail("admin" + i + "@wave.com");
                    admin.setPassword(passwordEncoder.encode("Admin@123"));
                    admin.setSolde(BigDecimal.ZERO);
                    admin.setRole(roleAdmin);
                    admin.setEstActif(true);
                    admin.setTypeNotification(NotificationType.EMAIL);
                    String qrCode = qrCodeGenerator.generateQRCodeBase64(telephone);
                    admin.setCodeQr(qrCode);
                    utilisateurRepository.save(admin);
                }
                
                // Create 10 Clients
                for (int i = 1; i <= 10; i++) {
                    Utilisateur client = new Utilisateur();
                    String telephone = String.format("+22177%07d", i);
                    client.setNumeroTelephone(telephone);
                    client.setNomComplet("Client " + i);
                    client.setEmail("client" + i + "@example.com");
                    client.setPassword(passwordEncoder.encode("Client@123"));
                    client.setSolde(new BigDecimal("10000"));
                    client.setRole(roleClient);
                    client.setEstActif(true);
                    client.setTypeNotification(NotificationType.SMS);
                    String qrCode = qrCodeGenerator.generateQRCodeBase64(telephone);
                    client.setCodeQr(qrCode);
                    utilisateurRepository.save(client);
                }
                
                // Create 3 Agents
                for (int i = 1; i <= 3; i++) {
                    Utilisateur agent = new Utilisateur();
                    String telephone = String.format("+22178%07d", i);
                    agent.setNumeroTelephone(telephone);
                    agent.setNomComplet("Agent " + i);
                    agent.setEmail("agent" + i + "@wave.com");
                    agent.setPassword(passwordEncoder.encode("Agent@123"));
                    agent.setSolde(new BigDecimal("1000000"));
                    agent.setRole(roleAgent);
                    agent.setEstActif(true);
                    agent.setTypeNotification(NotificationType.WHATSAPP);
                    String qrCode = qrCodeGenerator.generateQRCodeBase64(telephone);
                    agent.setCodeQr(qrCode);
                    utilisateurRepository.save(agent);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Erreur lors du seeding des utilisateurs: " + e.getMessage());
            }
        }
    }
}
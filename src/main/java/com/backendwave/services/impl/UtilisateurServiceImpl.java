package com.backendwave.services.impl;


import com.backendwave.data.entities.Plafond;
import com.backendwave.data.entities.Role;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.repositories.PlafondRepository;
import com.backendwave.data.repositories.RoleRepository;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.backendwave.services.UtilisateurService;
import com.backendwave.services.PDFService;
import com.backendwave.services.EmailService;
import com.backendwave.utils.QRCodeGenerator;
import com.backendwave.web.dto.request.users.CreateClientDto;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final QRCodeGenerator qrCodeGenerator;
    private final EmailService emailService;
    private final PDFService pdfService;
    private final PlafondRepository plafondRepository;

    @Override
    @Transactional
    public Utilisateur createClient(CreateClientDto createClientDto) throws IllegalArgumentException {
        // Validation des données
        if (!createClientDto.getPassword().equals(createClientDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Les codes PIN ne correspondent pas");
        }

        if (existsByNumeroTelephone(createClientDto.getNumeroTelephone())) {
            throw new IllegalArgumentException("Ce numéro de téléphone est déjà utilisé");
        }

        if (createClientDto.getEmail() != null && !createClientDto.getEmail().isEmpty()
                && existsByEmail(createClientDto.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }

        // Récupérer le rôle CLIENT
        Role roleClient = roleRepository.findByNom("CLIENT")
                .orElseThrow(() -> new IllegalArgumentException("Rôle CLIENT non trouvé"));

        // Générer le QR code
        String qrCode = qrCodeGenerator.generateQRCodeBase64(createClientDto.getNumeroTelephone());

        // Créer l'utilisateur
        Utilisateur client = new Utilisateur();
        client.setNomComplet(createClientDto.getNomComplet());
        client.setNumeroTelephone(createClientDto.getNumeroTelephone());
        client.setEmail(createClientDto.getEmail());
        client.setPassword(passwordEncoder.encode(createClientDto.getPassword()));
        client.setRole(roleClient);
        client.setEstActif(true);
        client.setSolde(BigDecimal.ZERO);
        client.setCodeQr(qrCode);

        // Sauvegarder l'utilisateur
        Utilisateur savedClient = utilisateurRepository.save(client);

        // Créer le plafond associé à l'utilisateur
        Plafond plafond = new Plafond();
        plafond.setUtilisateur(savedClient);
        plafond.setLimiteJournaliere(new BigDecimal("2000000"));
        plafond.setLimiteMensuelle(new BigDecimal("30000000"));
        plafond.setMontantMaxTransaction(new BigDecimal("1000000"));
        plafondRepository.save(plafond);

        // Envoyer l'email de bienvenue avec le PDF
        if (savedClient.getEmail() != null && !savedClient.getEmail().isEmpty()) {
            // Générer le PDF
            byte[] pdfContent = pdfService.generateWelcomePDF(
                    savedClient.getNomComplet(),
                    qrCode,
                    plafond.getLimiteJournaliere(),
                    plafond.getLimiteMensuelle(),
                    plafond.getMontantMaxTransaction()
            );

            // Envoyer l'email avec le PDF en pièce jointe
            emailService.sendEmailWithAttachment(
                    savedClient.getEmail(),
                    "Bienvenue sur notre plateforme",
                    "Cher(e) " + savedClient.getNomComplet() + ",\n\n" +
                            "Nous sommes ravis de vous accueillir sur notre plateforme. " +
                            "Vous trouverez ci-joint un document contenant toutes les informations relatives à votre compte, " +
                            "y compris votre code QR et vos limites de transaction.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe BackendWave",
                    pdfContent,
                    "bienvenue.pdf"
            );
        }

        return savedClient;
    }
   // Méthodes existantes...
   @Override
   public Utilisateur save(Utilisateur utilisateur) {
       return utilisateurRepository.save(utilisateur);
   }

   @Override
   public Optional<Utilisateur> findById(Long id) {
       return utilisateurRepository.findById(id);
   }

   @Override
   public List<Utilisateur> findAll() {
       return utilisateurRepository.findAll();
   }

   @Override
   public void deleteById(Long id) {
       utilisateurRepository.deleteById(id);
   }

   @Override
   public Optional<Utilisateur> findByNumeroTelephone(String numeroTelephone) {
       return utilisateurRepository.findByNumeroTelephone(numeroTelephone);
   }

   @Override
   public Optional<Utilisateur> findByEmail(String email) {
       return utilisateurRepository.findByEmail(email);
   }

   @Override
   public List<Utilisateur> findByRoleId(Long roleId) {
       return utilisateurRepository.findByRoleId(roleId);
   }

   @Override
   public List<Utilisateur> findActiveUsers() {
       return utilisateurRepository.findByEstActifTrue();
   }

   @Override
   public boolean existsByNumeroTelephone(String numeroTelephone) {
       return utilisateurRepository.existsByNumeroTelephone(numeroTelephone);
   }

   @Override
   public boolean existsByEmail(String email) {
       return utilisateurRepository.existsByEmail(email);
   }
}
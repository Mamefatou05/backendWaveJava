package com.backendwave.services.impl;

import com.backendwave.data.entities.Role;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.repositories.RoleRepository;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.backendwave.services.UtilisateurService;
import com.backendwave.utils.QRCodeGenerator;
import com.backendwave.web.dto.request.users.CreateClientDto;
import com.backendwave.services.EmailService;
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

   @Override
   public Utilisateur createClient(CreateClientDto createClientDto) throws IllegalArgumentException {
       // Vérifier si les mots de passe correspondent
       if (!createClientDto.getPassword().equals(createClientDto.getConfirmPassword())) {
           throw new IllegalArgumentException("Les codes PIN ne correspondent pas");
       }

       // Vérifier si le numéro existe déjà
       if (existsByNumeroTelephone(createClientDto.getNumeroTelephone())) {
           throw new IllegalArgumentException("Ce numéro de téléphone est déjà utilisé");
       }

       // Vérifier si l'email existe déjà (si fourni)
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
       client.setCodeQr(qrCode); // Ajout du QR code

       // Sauvegarder et retourner le client
       Utilisateur savedClient = save(client);

       // Envoyer un email au client avec le code QR
       String emailContent = "Votre compte a été créé avec succès !\nVoici votre code QR : " + qrCode;
       emailService.sendEmail(savedClient.getEmail(), "Bienvenue", emailContent);

       return savedClient; // Retourner l'utilisateur sauvegardé
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
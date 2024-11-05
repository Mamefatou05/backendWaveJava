package com.backendwave.services;

import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Utilisateur> userOptional;

        // Vérifiez si le username est un email ou un numéro de téléphone
        if (username.contains("@")) {
            // Si c'est un email, recherchez par email
            userOptional = userRepository.findByEmail(username);
        } else {
            // Sinon, recherchez par numéro de téléphone
            userOptional = userRepository.findByNumeroTelephone(username);
        }

        Utilisateur user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),  // Utilisation de l'email comme nom d'utilisateur
                user.getMotDePasseHash(),  // Mot de passe
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getNom()))  // Créer une autorité à partir du rôle
        );
    }

}

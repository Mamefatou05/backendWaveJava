package com.backendwave.services.impl;

import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.repositories.UtilisateurRepository;
import com.backendwave.services.UtilisateurService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

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
        return utilisateurRepository.findByRole_Id(roleId);
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

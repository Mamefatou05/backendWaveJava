package com.backendwave.services.impl;

import com.backendwave.data.entities.Plafond;
import com.backendwave.data.repositories.PlafondRepository;
import com.backendwave.services.PlafondService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class PlafondServiceImpl implements PlafondService {

    private final PlafondRepository plafondRepository;

    @Autowired
    public PlafondServiceImpl(PlafondRepository plafondRepository) {
        this.plafondRepository = plafondRepository;
    }

    @Override
    public Plafond save(Plafond plafond) {
        return plafondRepository.save(plafond);
    }

    @Override
    public Plafond findById(Long id) {
        return plafondRepository.findById(id).orElse(null);
    }

    @Override
    public List<Plafond> findAll() {
        return plafondRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        plafondRepository.deleteById(id);
    }

    @Override
    public Optional<Plafond> findByUtilisateurId(Long utilisateurId) {
        return plafondRepository.findByUtilisateur_Id(utilisateurId);
    }
}

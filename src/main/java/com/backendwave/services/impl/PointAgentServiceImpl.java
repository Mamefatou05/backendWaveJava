package com.backendwave.services.impl;

import com.backendwave.data.entities.PointAgent;
import com.backendwave.data.repositories.PointAgentRepository;
import com.backendwave.services.PointAgentService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class PointAgentServiceImpl implements PointAgentService {

    private final PointAgentRepository pointAgentRepository;

    @Autowired
    public PointAgentServiceImpl(PointAgentRepository pointAgentRepository) {
        this.pointAgentRepository = pointAgentRepository;
    }

    @Override
    public PointAgent save(PointAgent pointAgent) {
        return pointAgentRepository.save(pointAgent);
    }

    @Override
    public Optional<PointAgent> findById(Long id) {
        return pointAgentRepository.findById(id);
    }

    @Override
    public List<PointAgent> findAll() {
        return pointAgentRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        pointAgentRepository.deleteById(id);
    }

    @Override
    public List<PointAgent> findActivePoints() {
        return pointAgentRepository.findByEstActifTrue();
    }

    @Override
    public Optional<PointAgent> findByAdresse(String adresse) {
        return pointAgentRepository.findByAdresse(adresse);
    }
}

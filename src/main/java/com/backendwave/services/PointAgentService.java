package com.backendwave.services;

import com.backendwave.data.entities.PointAgent;
import java.util.List;
import java.util.Optional;

public interface PointAgentService {
    PointAgent save(PointAgent pointAgent);
    Optional<PointAgent> findById(Long id);
    List<PointAgent> findAll();
    void deleteById(Long id);
    List<PointAgent> findActivePoints();
    Optional<PointAgent> findByAdresse(String adresse);
}

package com.backendwave.services;

import com.backendwave.data.entities.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role save(Role role);
    Optional<Role> findById(Long id);
    List<Role> findAll();
    void deleteById(Long id);
    Optional<Role> findByNom(String nom);
    List<Role> findByNomContainingIgnoreCase(String nom);
    boolean existsByNom(String nom);
}

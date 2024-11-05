package com.backendwave.services.impl;

import com.backendwave.data.entities.Role;
import com.backendwave.data.repositories.RoleRepository;
import com.backendwave.services.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Optional<Role> findByNom(String nom) {
        return roleRepository.findByNom(nom);
    }

    @Override
    public List<Role> findByNomContainingIgnoreCase(String nom) {
        return roleRepository.findByNomContainingIgnoreCase(nom);
    }

    @Override
    public boolean existsByNom(String nom) {
        return roleRepository.existsByNom(nom);
    }
}

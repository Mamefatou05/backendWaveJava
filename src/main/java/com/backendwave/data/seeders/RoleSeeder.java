package com.backendwave.data.seeders;

import com.backendwave.data.entities.Role;
import com.backendwave.data.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder {
    
    private final RoleRepository roleRepository;
    
    public void seed() {
        if (roleRepository.count() == 0) {
            createRole("ADMIN");
            createRole("CLIENT");
            createRole("AGENT");
        }
    }
    
    private void createRole(String nom) {
        Role role = new Role();
        role.setNom(nom);
        roleRepository.save(role);
    }
} 
package com.backendwave.data.seeders;

import com.backendwave.data.entities.Role;
import com.backendwave.data.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder {
    
    @Autowired
    private RoleRepository roleRepository;
    
    public void seed() {
        if (roleRepository.count() == 0) {
            Role roleClient = new Role();
            roleClient.setNom("CLIENT");
            roleRepository.save(roleClient);
            
            Role roleAdmin = new Role();
            roleAdmin.setNom("ADMIN");
            roleRepository.save(roleAdmin);
            
            Role roleAgent = new Role();
            roleAgent.setNom("AGENT");
            roleRepository.save(roleAgent);
        }
    }
}
package com.backendwave.data.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleSeeder roleSeeder;
    private final UtilisateurSeeder utilisateurSeeder;
    private final PlafondSeeder plafondSeeder;
    private final PointAgentSeeder pointAgentSeeder;

    @Override
    public void run(String... args) {
        if (shouldSeed()) {
            roleSeeder.seed();
            utilisateurSeeder.seed();
            plafondSeeder.seed();
            pointAgentSeeder.seed();
        }
    }

    private boolean shouldSeed() {
        return true;
    }
} 
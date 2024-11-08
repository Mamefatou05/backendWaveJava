package com.backendwave;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.backendwave", "com.mfn.mydependance"})
@EnableScheduling  // Activer la planification de tâches
public class BackendwaveApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendwaveApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("Application en marche");
    }

}
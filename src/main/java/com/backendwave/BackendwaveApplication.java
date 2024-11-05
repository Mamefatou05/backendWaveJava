package com.backendwave;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.backendwave", "com.mfn.mydependance"})
public class BackendwaveApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendwaveApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("Application en marche");
    }

}
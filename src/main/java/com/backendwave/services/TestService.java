package com.backendwave.services;

import org.springframework.stereotype.Service;

@Service
public class TestService {

    public String Bonjour() {
        return "Bonjour depuis ma Dependance Spring vous me recevez!";
    }
}

package com.backendwave.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;



public interface TokenService {
    String generateToken(Authentication authentication);
    String getUsernameFromJWT(String token);
    Claims getClaimsFromJWT(String token);
    boolean validateToken(String authToken);
}
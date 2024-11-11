package com.backendwave.services.impl;

import com.backendwave.services.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenServiceImpl implements TokenService {

    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.jwt.expiration}")
    private int jwtExpirationInMs;

    // Méthode pour convertir la clé secrète en objet Key
    private Key getSigningKey() {
        // Convertir la clé secrète en tableau d'octets
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes); // Utiliser Keys.hmacShaKeyFor
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        // Extraire les rôles
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) // Ajouter les rôles en tant que claim
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // Utiliser la clé générée et HS512
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException | SignatureException | ExpiredJwtException e) {
            // Gestion des erreurs
        }
        return false;
    }

    public Claims getClaimsFromJWT(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    public String getRolesFromJWT(String token) {
        Claims claims = getClaimsFromJWT(token);
        return (String) claims.get("roles"); // Récupérer les rôles
    }
}
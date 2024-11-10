package com.backendwave.web.controllers.impl;

import com.backendwave.web.dto.response.JwtAuthenticationResponse;
import com.backendwave.web.dto.request.users.Login;
import com.mfn.mydependance.services.AuthService;
import com.mfn.mydependance.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService jwtTokenService;
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Login loginRequest) {
        Authentication authentication = authService.authenticateUser(loginRequest);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenService.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(accessToken));
    }

//    @PostMapping("/login/refresh")
//    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
//        if (!jwtTokenService.validateToken(refreshToken)) {
//            throw new RuntimeException("Refresh token invalide");
//        }
//
//        String username = jwtTokenService.getUsernameFromJWT(refreshToken);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//        UsernamePasswordAuthenticationToken authentication =
//                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//        String newAccessToken = jwtTokenService.generateToken(authentication);
//        String newRefreshToken = jwtTokenService.generateRefreshToken(authentication);
//
//        return ResponseEntity.ok(new JwtAuthenticationResponse(newAccessToken, newRefreshToken));
//    }
}

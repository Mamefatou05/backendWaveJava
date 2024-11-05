package com.backendwave.web.controllers.impl;

import com.backendwave.web.dto.response.JwtAuthenticationResponse;
import com.backendwave.web.dto.request.users.Login;
import com.mfn.mydependance.services.AuthService;
import com.mfn.mydependance.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService jwtTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Login loginRequest) {
        Authentication authentication = authService.authenticateUser(loginRequest);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenService.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
}

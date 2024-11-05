package com.backendwave.web.dto.response;


public class JwtAuthenticationResponse {
    private final String accessToken;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}

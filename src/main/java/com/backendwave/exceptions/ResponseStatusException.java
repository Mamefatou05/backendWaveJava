package com.backendwave.exceptions;


import org.springframework.http.HttpStatus;

public class ResponseStatusException extends RuntimeException {
    private final HttpStatus status;

    public ResponseStatusException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

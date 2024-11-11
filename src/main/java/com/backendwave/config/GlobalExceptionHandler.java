package com.backendwave.config;

import com.backendwave.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Gestion des exceptions pour l'accès non autorisé
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException ex) {
        return buildErrorResponse("Accès non autorisé", HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    // Gestion des exceptions pour l'accès refusé (permission)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        return buildErrorResponse("Accès refusé", HttpStatus.FORBIDDEN, ex.getMessage());
    }

    // Gestion des autres exceptions (existant dans votre code)
    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleModelNotFoundException(ModelNotFoundException ex) {
        return buildErrorResponse("Ressource non trouvée", HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, Object>> handleServiceException(ServiceException ex) {
        return buildErrorResponse("Erreur dans le service", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        return buildErrorResponse("Erreur interne du serveur", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildErrorResponse("Entité non trouvée", HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Gestion des exceptions de validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "ECHEC");
        response.put("message", "Erreur de validation");
        response.put("data", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        return buildErrorResponse("Erreur de validation", HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Méthode utilitaire pour construire les réponses d'erreur
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, String error) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ECHEC");
        response.put("message", message);
        response.put("data", error);
        return ResponseEntity.status(status).body(response);
    }
}

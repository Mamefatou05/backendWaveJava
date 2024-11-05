package com.backendwave.data.repositories;

import com.backendwave.data.entities.CodeVerification;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CodeVerificationRepository extends BaseRepository<CodeVerification> {
    
    // Trouver un code par utilisateur et code qui n'est pas expiré et non utilisé
    Optional<CodeVerification> findByUtilisateurIdAndCodeAndDateExpirationAfterAndEstUtiliseFalse(
        Long utilisateurId, 
        String code, 
        LocalDateTime now
    );
    
    // Trouver les codes actifs d'un utilisateur
    Optional<CodeVerification> findByUtilisateurIdAndEstUtiliseFalseAndDateExpirationAfter(
        Long utilisateurId, 
        LocalDateTime now
    );
} 
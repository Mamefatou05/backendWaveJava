package com.backendwave.data.repositories;

import com.backendwave.data.entities.CodesVerification;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CodesVerificationRepository extends BaseRepository<CodesVerification> {
    
    // Trouver un code par utilisateur et code qui n'est pas expiré et non utilisé
    Optional<CodesVerification> findByUtilisateurIdAndCodeAndDateExpirationAfterAndEstUtiliseFalse(
        Long utilisateurId, 
        String code, 
        LocalDateTime now
    );
    
    // Trouver les codes actifs d'un utilisateur
    Optional<CodesVerification> findByUtilisateurIdAndEstUtiliseFalseAndDateExpirationAfter(
        Long utilisateurId, 
        LocalDateTime now
    );
} 
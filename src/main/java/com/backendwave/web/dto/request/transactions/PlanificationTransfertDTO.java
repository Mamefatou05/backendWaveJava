package com.backendwave.web.dto.request.transactions;

import com.backendwave.data.enums.Periodicity;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class PlanificationTransfertDTO {

    private String expediteurTelephone;  // Numéro de téléphone de l'expéditeur
    private String destinataireTelephone;  // Numéro de téléphone du destinataire
    private BigDecimal montant;  // Montant du transfert
    private Periodicity periodicite;  // Périodicité de la planification (journalier, hebdomadaire, mensuel, etc.)
    private String referenceGroupe;  // Référence de groupe (facultatif)
    private LocalTime heureExecution;  // Heure d'exécution de la planification
}

package com.backendwave.services;

import com.backendwave.data.entities.PlanificationTransfert;
import com.backendwave.web.dto.request.transactions.PlanificationTransfertDTO;

public interface PlanificationTransfertService {
    void traiterPlanification(PlanificationTransfert planification);
    void relancerPlanification(Long planificationId);
    void annulerPlanification(Long planificationId);
    void desactiverPlanification(Long planificationId);
    PlanificationTransfert creerPlanification(PlanificationTransfertDTO planificationDTO);
}

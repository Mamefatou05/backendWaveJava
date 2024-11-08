package com.backendwave.web.controllers.impl;



import com.backendwave.data.entities.PlanificationTransfert;
import com.backendwave.services.PlanificationTransfertService;
import com.backendwave.web.controllers.PlanificationTransfertController;
import com.backendwave.web.dto.request.transactions.PlanificationTransfertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planification")
public class PlanificationTransfertControllerImpl implements PlanificationTransfertController {

    @Autowired
    private PlanificationTransfertService planificationTransfertService;

    @PostMapping("/relancer/{id}")
    public void relancerTransaction(@PathVariable Long id) {
        planificationTransfertService.relancerPlanification(id);
    }

    @PostMapping("/annuler/{id}")
    public void annulerTransaction(@PathVariable Long id) {
        planificationTransfertService.annulerPlanification(id);
    }

    @PostMapping("/desactiver/{id}")
    public void desactiverTransaction(@PathVariable Long id) {
        planificationTransfertService.desactiverPlanification(id);
    }

    @PostMapping
    public PlanificationTransfert createPlanification(@RequestBody PlanificationTransfertDTO planificationDTO) {
        return planificationTransfertService.creerPlanification(planificationDTO);
    }
}

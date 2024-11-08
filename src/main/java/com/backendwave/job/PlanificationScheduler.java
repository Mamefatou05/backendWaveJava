package com.backendwave.job;

import com.backendwave.data.entities.PlanificationTransfert;
import com.backendwave.data.repositories.PlanificationTransfertRepository;
import com.backendwave.services.PlanificationTransfertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PlanificationScheduler {

    @Autowired
    private PlanificationTransfertService planificationTransfertService;

    @Autowired
    private PlanificationTransfertRepository planificationRepository;

    // Exemple : exécution quotidienne
    @Scheduled(cron = "*/1 * * * * ?")
    public void executeScheduledTransactions() {
       // System.out.println("azerty dans je scheduledTransactions");
//        System.out.printf(String.valueOf(LocalDateTime.now()));

        List<PlanificationTransfert> planifications = planificationRepository.findByProchaineExecutionBeforeAndEstActif(LocalDateTime.now());
//        System.out.printf(planifications.toString());
        for (PlanificationTransfert planification : planifications) {
            planificationTransfertService.traiterPlanification(planification);
        }
    }
}

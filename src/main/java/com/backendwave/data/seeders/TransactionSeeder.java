package com.backendwave.data.seeders;

import com.backendwave.data.entities.Transaction;
import com.backendwave.data.entities.Utilisateur;
import com.backendwave.data.enums.TransactionType;
import com.backendwave.data.enums.TransactionStatus;
import com.backendwave.data.repositories.TransactionRepository;
import com.backendwave.data.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class TransactionSeeder {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public void seed() {
        if (transactionRepository.count() == 0) {
            List<Utilisateur> clients = utilisateurRepository.findByRoleId(1L);
            List<Utilisateur> agents = utilisateurRepository.findByRoleId(2L);
            Random random = new Random();

            // Créer 20 transactions aléatoires
            for (int i = 0; i < 10; i++) {
                Transaction transaction = new Transaction();

                // Alterner entre DEPOT et RETRAIT
                TransactionType type = (i % 2 == 0) ? TransactionType.DEPOT : TransactionType.RETRAIT;
                transaction.setTypeTransaction(type);

                // Montant aléatoire entre 1000 et 50000
                BigDecimal montant = new BigDecimal(1000 + random.nextInt(49001));
                transaction.setMontant(montant);

                // Sélectionner client et agent aléatoirement
                Utilisateur client = clients.get(random.nextInt(clients.size()));
                Utilisateur agent = agents.get(random.nextInt(agents.size()));

                transaction.setExpediteur(client);
                transaction.setDestinataire(agent);
                transaction.setProchaineExecution(LocalDateTime.now().minusDays(random.nextInt(30)));
                transaction.setFraisTransfert(montant.multiply(new BigDecimal("0.01"))); // 1% de frais
                transaction.setStatut(TransactionStatus.COMPLETE);

                transactionRepository.save(transaction);
            }
        }
    }
}
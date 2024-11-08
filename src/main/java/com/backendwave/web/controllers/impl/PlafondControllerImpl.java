package com.backendwave.web.controllers.impl;

import com.backendwave.data.entities.Plafond;
import com.backendwave.services.PlafondService;
import com.backendwave.web.controllers.PlafondController;

import com.backendwave.web.dto.request.transactions.PlafondRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/plafonds")
public class PlafondControllerImpl implements PlafondController {

    private final PlafondService plafondService;

    @Autowired
    public PlafondControllerImpl(PlafondService plafondService) {
        this.plafondService = plafondService;
    }

    @Override
    @GetMapping("/{utilisateurId}")
    public Optional<Plafond> getPlafondByUtilisateurId(@PathVariable Long utilisateurId) {
        return plafondService.getPlafondByUtilisateurId(utilisateurId);
    }

    @Override
    @PutMapping("/{utilisateurId}")
    public Plafond modifierPlafond(
            @PathVariable Long utilisateurId,
            @RequestBody PlafondRequestDto plafondRequestDto) {
        return plafondService.modifierPlafond(utilisateurId, plafondRequestDto);
    }
}

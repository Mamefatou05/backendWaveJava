package com.backendwave.services;

import com.backendwave.data.entities.Plafond;
import com.backendwave.web.dto.request.transactions.PlafondRequestDto;

import java.math.BigDecimal;
import java.util.Optional;

public interface PlafondService {

    Optional<Plafond> getPlafondByUtilisateurId(Long utilisateurId);

    Plafond modifierPlafond(Long utilisateurId, PlafondRequestDto plafondRequestDto);
}

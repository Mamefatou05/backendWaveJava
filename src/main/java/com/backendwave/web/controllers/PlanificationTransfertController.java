package com.backendwave.web.controllers;

import org.springframework.web.bind.annotation.PathVariable;

public interface PlanificationTransfertController {
    void relancerTransaction(@PathVariable Long id) ;
    void annulerTransaction(@PathVariable Long id) ;
    void desactiverTransaction(@PathVariable Long id) ;
}

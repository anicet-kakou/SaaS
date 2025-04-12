package com.devolution.saas.insurance.nonlife.auto.api.controller;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.application.dto.BonusMalusDTO;
import com.devolution.saas.insurance.nonlife.auto.application.usecase.ApplyBonusMalus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Contrôleur REST pour la gestion du bonus-malus automobile.
 */
@RestController
@RequestMapping("/api/v1/auto/bonus-malus")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auto Bonus-Malus", description = "API pour la gestion du bonus-malus automobile")
public class AutoBonusMalusController {

    private final ApplyBonusMalus applyBonusMalus;

    /**
     * Récupère le bonus-malus d'un client.
     *
     * @param customerId     L'ID du client
     * @param organizationId L'ID de l'organisation
     * @return Le bonus-malus du client
     */
    @GetMapping("/{customerId}")
    @Operation(summary = "Récupère le bonus-malus d'un client")
    @Auditable(action = "API_GET_AUTO_BONUS_MALUS")
    @TenantRequired
    public ResponseEntity<BonusMalusDTO> getBonusMalus(
            @PathVariable UUID customerId,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour récupérer le bonus-malus du client: {} pour l'organisation: {}",
                customerId, organizationId);

        // Récupère le bonus-malus actuel (0 sinistre)
        BonusMalusDTO bonusMalus = applyBonusMalus.execute(customerId, 0, organizationId);
        return ResponseEntity.ok(bonusMalus);
    }

    /**
     * Recalcule le coefficient de bonus-malus.
     *
     * @param customerId     L'ID du client
     * @param claimCount     Le nombre de sinistres responsables
     * @param organizationId L'ID de l'organisation
     * @return Le bonus-malus mis à jour
     */
    @PostMapping("/{customerId}/calculate")
    @Operation(summary = "Recalcule le coefficient de bonus-malus")
    @Auditable(action = "API_CALCULATE_AUTO_BONUS_MALUS")
    @TenantRequired
    public ResponseEntity<BonusMalusDTO> calculateBonusMalus(
            @PathVariable UUID customerId,
            @RequestParam int claimCount,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour calculer le bonus-malus du client: {} avec {} sinistre(s) pour l'organisation: {}",
                customerId, claimCount, organizationId);

        BonusMalusDTO bonusMalus = applyBonusMalus.execute(customerId, claimCount, organizationId);
        return ResponseEntity.ok(bonusMalus);
    }
}

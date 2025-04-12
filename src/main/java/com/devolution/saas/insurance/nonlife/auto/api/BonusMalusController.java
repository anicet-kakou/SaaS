package com.devolution.saas.insurance.nonlife.auto.api;

import com.devolution.saas.insurance.nonlife.auto.api.controller.AutoBonusMalusController;
import com.devolution.saas.insurance.nonlife.auto.application.dto.BonusMalusDTO;
import com.devolution.saas.insurance.nonlife.auto.application.usecase.ApplyBonusMalus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Contrôleur REST pour la gestion du bonus-malus.
 *
 * @deprecated Utiliser {@link AutoBonusMalusController} à la place.
 * Ce contrôleur sera supprimé dans une version future.
 */
@Deprecated(since = "1.0.0", forRemoval = true)
@RestController
@RequestMapping("/api/v1/auto/bonus-malus")
@RequiredArgsConstructor
public class BonusMalusController {

    private final ApplyBonusMalus applyBonusMalus;

    /**
     * Récupère le bonus-malus d'un client.
     *
     * @param customerId     L'ID du client
     * @param organizationId L'ID de l'organisation
     * @return Le bonus-malus du client
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<BonusMalusDTO> getBonusMalus(@PathVariable UUID customerId,
                                                       @RequestParam UUID organizationId) {
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
    public ResponseEntity<BonusMalusDTO> calculateBonusMalus(@PathVariable UUID customerId,
                                                             @RequestParam int claimCount,
                                                             @RequestParam UUID organizationId) {
        BonusMalusDTO bonusMalus = applyBonusMalus.execute(customerId, claimCount, organizationId);
        return ResponseEntity.ok(bonusMalus);
    }
}

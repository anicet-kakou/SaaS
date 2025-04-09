package com.devolution.saas.insurance.nonlife.auto.api;

import com.devolution.saas.insurance.nonlife.auto.application.service.AutoPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des polices auto.
 */
@RestController
@RequestMapping("/api/v1/auto/policies")
@RequiredArgsConstructor
public class AutoPolicyController {

    private final AutoPricingService autoPricingService;

    /**
     * Calcule la prime pour une police auto.
     *
     * @param vehicleId             L'ID du véhicule
     * @param coverageType          Le type de couverture (tiers ou tous risques)
     * @param bonusMalusCoefficient Le coefficient de bonus-malus
     * @param additionalOptions     Les options additionnelles
     * @param organizationId        L'ID de l'organisation
     * @return La prime calculée
     */
    @PostMapping("/calculate")
    public ResponseEntity<BigDecimal> calculatePremium(@RequestParam UUID vehicleId,
                                                       @RequestParam String coverageType,
                                                       @RequestParam BigDecimal bonusMalusCoefficient,
                                                       @RequestBody Map<String, Boolean> additionalOptions,
                                                       @RequestParam UUID organizationId) {
        BigDecimal premium = autoPricingService.calculateFinalPremium(vehicleId, coverageType,
                bonusMalusCoefficient, additionalOptions,
                organizationId);
        return ResponseEntity.ok(premium);
    }

    /**
     * Simule différentes options de tarification.
     *
     * @param vehicleId      L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     * @return Les différentes options de tarification
     */
    @PostMapping("/simulate")
    public ResponseEntity<Map<String, BigDecimal>> simulatePricing(@RequestParam UUID vehicleId,
                                                                   @RequestParam UUID organizationId) {
        Map<String, BigDecimal> pricingOptions = autoPricingService.simulatePricingOptions(vehicleId, organizationId);
        return ResponseEntity.ok(pricingOptions);
    }
}

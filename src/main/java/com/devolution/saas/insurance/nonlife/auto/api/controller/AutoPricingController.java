package com.devolution.saas.insurance.nonlife.auto.api.controller;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.annotation.TenantRequired;
import com.devolution.saas.insurance.nonlife.auto.application.service.AutoPricingService;
import com.devolution.saas.common.util.Validation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Contrôleur REST pour la tarification automobile.
 */
@RestController
@RequestMapping("/api/v1/auto/pricing")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auto Pricing", description = "API pour la tarification automobile")
public class AutoPricingController {

    // Using the shared validation pattern from Validation
    private static final Pattern COVERAGE_TYPE_PATTERN = Validation.COVERAGE_TYPE_PATTERN;
    private final AutoPricingService autoPricingService;

    /**
     * Calcule la prime de base pour un véhicule.
     *
     * @param vehicleId      L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     * @return La prime de base
     */
    @GetMapping("/base-premium")
    @Operation(summary = "Calcule la prime de base pour un véhicule")
    @Auditable(action = "API_CALCULATE_BASE_PREMIUM")
    @TenantRequired
    public ResponseEntity<BigDecimal> calculateBasePremium(
            @RequestParam UUID vehicleId,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour calculer la prime de base pour le véhicule: {} pour l'organisation: {}",
                vehicleId, organizationId);

        // Validate input parameters
        Validation.validateNotNull(vehicleId, "ID du véhicule");
        Validation.validateNotNull(organizationId, "ID de l'organisation");

        BigDecimal basePremium = autoPricingService.calculateBasePremium(vehicleId, organizationId);
        return ResponseEntity.ok(basePremium);
    }

    /**
     * Calcule la prime finale pour un véhicule.
     *
     * @param vehicleId             L'ID du véhicule
     * @param coverageType          Le type de couverture
     * @param bonusMalusCoefficient Le coefficient de bonus-malus
     * @param additionalOptions     Les options additionnelles
     * @param organizationId        L'ID de l'organisation
     * @return La prime finale
     */
    @GetMapping("/final-premium")
    @Operation(summary = "Calcule la prime finale pour un véhicule")
    @Auditable(action = "API_CALCULATE_FINAL_PREMIUM")
    @TenantRequired
    public ResponseEntity<BigDecimal> calculateFinalPremium(
            @RequestParam UUID vehicleId,
            @RequestParam String coverageType,
            @RequestParam BigDecimal bonusMalusCoefficient,
            @RequestParam(required = false) Map<String, Boolean> additionalOptions,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour calculer la prime finale pour le véhicule: {} avec couverture: {} pour l'organisation: {}",
                vehicleId, coverageType, organizationId);

        // Validate input parameters
        Validation.validateNotNull(vehicleId, "ID du véhicule");
        Validation.validateNotEmpty(coverageType, "type de couverture");
        Validation.validatePattern(coverageType, COVERAGE_TYPE_PATTERN, "Type de couverture",
                "Les valeurs valides sont THIRD_PARTY et COMPREHENSIVE");
        Validation.validateNotNull(bonusMalusCoefficient, "coefficient bonus-malus");
        Validation.validateRange(bonusMalusCoefficient,
                new BigDecimal("0.50"),
                new BigDecimal("3.50"),
                "coefficient bonus-malus");
        Validation.validateNotNull(organizationId, "ID de l'organisation");

        // Ensure additionalOptions is not null before passing to service
        Map<String, Boolean> options = additionalOptions != null ? additionalOptions : new HashMap<>();

        BigDecimal finalPremium = autoPricingService.calculateFinalPremium(
                vehicleId, coverageType, bonusMalusCoefficient, options, organizationId);
        return ResponseEntity.ok(finalPremium);
    }

    /**
     * Simule les options de tarification pour un véhicule.
     *
     * @param vehicleId      L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     * @return Les options de tarification
     */
    @GetMapping("/simulate")
    @Operation(summary = "Simule les options de tarification pour un véhicule")
    @Auditable(action = "API_SIMULATE_PRICING_OPTIONS")
    @TenantRequired
    public ResponseEntity<Map<String, BigDecimal>> simulatePricingOptions(
            @RequestParam UUID vehicleId,
            @RequestParam UUID organizationId) {
        log.debug("REST request pour simuler les options de tarification pour le véhicule: {} pour l'organisation: {}",
                vehicleId, organizationId);

        // Validate input parameters
        Validation.validateNotNull(vehicleId, "ID du véhicule");
        Validation.validateNotNull(organizationId, "ID de l'organisation");

        Map<String, BigDecimal> options = autoPricingService.simulatePricingOptions(vehicleId, organizationId);
        return ResponseEntity.ok(options);
    }
}

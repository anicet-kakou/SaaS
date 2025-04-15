package com.devolution.saas.insurance.nonlife.auto.application.service.impl;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.common.util.Validation;
import com.devolution.saas.insurance.nonlife.auto.application.service.AutoPricingService;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.CoverageType;
import com.devolution.saas.insurance.nonlife.auto.domain.model.BonusMalusConstants;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.port.VehicleProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Implémentation du service de tarification auto.
 * Ce service d'application délègue la logique métier au service de domaine PricingCalculator.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AutoPricingServiceImpl implements AutoPricingService {

    private final VehicleProvider vehicleProvider;

    // Constante pour le type de couverture par défaut
    private static final CoverageType DEFAULT_COVERAGE_TYPE = CoverageType.THIRD_PARTY;

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateBasePremium(UUID vehicleId, UUID organizationId) {
        log.debug("Calcul de la prime de base pour le véhicule: {}", vehicleId);

        // Validate input parameters
        Validation.validateNotNull(vehicleId, "ID du véhicule");
        Validation.validateNotNull(organizationId, "ID de l'organisation");

        // Récupération du véhicule
        Vehicle vehicle = vehicleProvider.findVehicleById(vehicleId, organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", vehicleId));

        // Délégation au domaine pour le calcul de la prime de base
        // Par défaut, on utilise le type de couverture au tiers
        return vehicle.calculateBasePremium(DEFAULT_COVERAGE_TYPE);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateFinalPremium(UUID vehicleId, String coverageTypeStr, BigDecimal bonusMalusCoefficient,
                                            Map<String, Boolean> additionalOptions, UUID organizationId) {
        log.debug("Calcul de la prime finale pour le véhicule: {}, couverture: {}", vehicleId, coverageTypeStr);

        // Validate input parameters
        Validation.validateNotNull(vehicleId, "ID du véhicule");
        Validation.validateNotEmpty(coverageTypeStr, "type de couverture");
        Validation.validateNotNull(bonusMalusCoefficient, "coefficient bonus-malus");
        Validation.validateRange(bonusMalusCoefficient,
                BonusMalusConstants.MIN_COEFFICIENT,
                BonusMalusConstants.MAX_COEFFICIENT,
                "coefficient bonus-malus");
        Validation.validateNotNull(organizationId, "ID de l'organisation");

        // Récupération du véhicule
        Vehicle vehicle = vehicleProvider.findVehicleById(vehicleId, organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", vehicleId));

        // Conversion du type de couverture
        CoverageType coverageType;
        try {
            coverageType = CoverageType.valueOf(coverageTypeStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de couverture invalide: " + coverageTypeStr);
        }

        // Délégation au domaine pour le calcul de la prime finale
        return vehicle.calculateFinalPremium(coverageType, bonusMalusCoefficient, additionalOptions);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> simulatePricingOptions(UUID vehicleId, UUID organizationId) {
        log.debug("Simulation des options de tarification pour le véhicule: {}", vehicleId);

        // Validate input parameters
        Validation.validateNotNull(vehicleId, "ID du véhicule");
        Validation.validateNotNull(organizationId, "ID de l'organisation");

        // Récupération du véhicule
        Vehicle vehicle = vehicleProvider.findVehicleById(vehicleId, organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", vehicleId));

        // Délégation au domaine pour la simulation des options de tarification
        return vehicle.simulatePricingOptions();
    }
}

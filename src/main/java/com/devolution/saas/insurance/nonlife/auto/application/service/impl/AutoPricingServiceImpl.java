package com.devolution.saas.insurance.nonlife.auto.application.service.impl;

import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.application.service.AutoPricingService;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.CoverageType;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.port.VehicleProvider;
import com.devolution.saas.insurance.nonlife.auto.domain.service.PricingCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
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
    private final PricingCalculator pricingCalculator;

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateBasePremium(UUID vehicleId, UUID organizationId) {
        log.debug("Calcul de la prime de base pour le véhicule: {}", vehicleId);

        // Récupération du véhicule
        Vehicle vehicle = vehicleProvider.findVehicleById(vehicleId, organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", vehicleId));

        // Délégation du calcul au service de domaine
        return pricingCalculator.calculateBasePremium(vehicle, CoverageType.THIRD_PARTY);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateFinalPremium(UUID vehicleId, String coverageTypeStr, BigDecimal bonusMalusCoefficient,
                                            Map<String, Boolean> additionalOptions, UUID organizationId) {
        log.debug("Calcul de la prime finale pour le véhicule: {}, couverture: {}", vehicleId, coverageTypeStr);

        // Récupération du véhicule
        Vehicle vehicle = vehicleProvider.findVehicleById(vehicleId, organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", vehicleId));

        // Conversion du type de couverture
        CoverageType coverageType = CoverageType.valueOf(coverageTypeStr);

        // Délégation du calcul de la prime de base au service de domaine
        BigDecimal basePremium = pricingCalculator.calculateBasePremium(vehicle, coverageType);

        // Application du coefficient bonus-malus
        BigDecimal finalPremium = pricingCalculator.calculateFinalPremium(basePremium, bonusMalusCoefficient);

        // Application des options additionnelles (cette logique pourrait également être déplacée vers le domaine)
        if (additionalOptions != null) {
            if (Boolean.TRUE.equals(additionalOptions.get("glassCoverage"))) {
                finalPremium = finalPremium.add(new BigDecimal("80.00"));
            }
            if (Boolean.TRUE.equals(additionalOptions.get("assistanceCoverage"))) {
                finalPremium = finalPremium.add(new BigDecimal("120.00"));
            }
            if (Boolean.TRUE.equals(additionalOptions.get("driverCoverage"))) {
                finalPremium = finalPremium.add(new BigDecimal("150.00"));
            }
        }

        return finalPremium;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> simulatePricingOptions(UUID vehicleId, UUID organizationId) {
        log.debug("Simulation des options de tarification pour le véhicule: {}", vehicleId);

        // Récupération du véhicule
        Vehicle vehicle = vehicleProvider.findVehicleById(vehicleId, organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", vehicleId));

        Map<String, BigDecimal> options = new HashMap<>();

        // Calcul des primes pour différentes options en utilisant le service de domaine
        BigDecimal thirdPartyPremium = pricingCalculator.calculateBasePremium(vehicle, CoverageType.THIRD_PARTY);
        options.put("thirdPartyPremium", thirdPartyPremium);

        BigDecimal comprehensivePremium = pricingCalculator.calculateBasePremium(vehicle, CoverageType.COMPREHENSIVE);
        options.put("comprehensivePremium", comprehensivePremium);

        // Options additionnelles
        options.put("glassCoverage", new BigDecimal("80.00"));
        options.put("assistanceCoverage", new BigDecimal("120.00"));
        options.put("driverCoverage", new BigDecimal("150.00"));

        return options;
    }
}

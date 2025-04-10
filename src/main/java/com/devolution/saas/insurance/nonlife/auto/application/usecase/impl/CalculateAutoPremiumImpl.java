package com.devolution.saas.insurance.nonlife.auto.application.usecase.impl;

import com.devolution.saas.insurance.nonlife.auto.application.dto.PremiumCalculationResultDTO;
import com.devolution.saas.insurance.nonlife.auto.application.exception.AutoResourceNotFoundException;
import com.devolution.saas.insurance.nonlife.auto.application.usecase.CalculateAutoPremium;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.AutoPolicyRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.DriverRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.VehicleRepository;
import com.devolution.saas.insurance.nonlife.auto.domain.service.PremiumCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Implémentation du cas d'utilisation pour le calcul de la prime d'assurance auto.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CalculateAutoPremiumImpl implements CalculateAutoPremium {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final AutoPolicyRepository autoPolicyRepository;
    private final PremiumCalculator premiumCalculator;

    @Override
    @Transactional(readOnly = true)
    public PremiumCalculationResultDTO calculate(UUID vehicleId, UUID driverId, String coverageType, UUID organizationId) {
        log.debug("Calcul de la prime pour le véhicule: {}, conducteur: {}, couverture: {}",
                vehicleId, driverId, coverageType);

        // Récupération du véhicule
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .filter(v -> v.getOrganizationId().equals(organizationId))
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Véhicule", vehicleId));

        // Récupération du conducteur
        Driver driver = driverRepository.findById(driverId)
                .filter(d -> d.getOrganizationId().equals(organizationId))
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Conducteur", driverId));

        // Calcul de la prime de base
        BigDecimal basePremium = premiumCalculator.calculateBasePremium(vehicle, coverageType, organizationId);

        // Calcul des facteurs
        Map<String, BigDecimal> factors = premiumCalculator.calculateFactors(vehicle, driver, coverageType, organizationId);

        // Calcul de la prime ajustée
        BigDecimal adjustedPremium = premiumCalculator.calculateAdjustedPremium(basePremium, factors);

        // Détermination du coefficient de bonus-malus (par défaut 1.0)
        BigDecimal bonusMalusCoefficient = BigDecimal.ONE;

        // Calcul de la prime finale
        BigDecimal finalPremium = premiumCalculator.calculateFinalPremium(adjustedPremium, bonusMalusCoefficient);

        // Calcul des garanties
        Map<String, BigDecimal> coverages = premiumCalculator.calculateCoverages(finalPremium, coverageType, organizationId);

        // Construction du résultat
        return PremiumCalculationResultDTO.builder()
                .vehicleId(vehicleId)
                .driverId(driverId)
                .coverageType(coverageType)
                .basePremium(basePremium)
                .bonusMalusCoefficient(bonusMalusCoefficient)
                .adjustedPremium(adjustedPremium)
                .finalPremium(finalPremium)
                .factors(factors)
                .coverages(coverages)
                .organizationId(organizationId)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PremiumCalculationResultDTO calculateForPolicy(UUID policyId, UUID organizationId) {
        log.debug("Calcul de la prime pour la police: {}", policyId);

        // Récupération de la police
        AutoPolicy policy = autoPolicyRepository.findById(policyId)
                .filter(p -> p.getOrganizationId().equals(organizationId))
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Police", policyId));

        // Récupération du véhicule
        Vehicle vehicle = vehicleRepository.findById(policy.getVehicleId())
                .filter(v -> v.getOrganizationId().equals(organizationId))
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Véhicule", policy.getVehicleId()));

        // Récupération du conducteur
        Driver driver = driverRepository.findById(policy.getPrimaryDriverId())
                .filter(d -> d.getOrganizationId().equals(organizationId))
                .orElseThrow(() -> AutoResourceNotFoundException.forId("Conducteur", policy.getPrimaryDriverId()));

        // Calcul de la prime de base
        BigDecimal basePremium = premiumCalculator.calculateBasePremium(vehicle, policy.getCoverageType().name(), organizationId);

        // Calcul des facteurs
        Map<String, BigDecimal> factors = premiumCalculator.calculateFactors(vehicle, driver, policy.getCoverageType().name(), organizationId);

        // Calcul de la prime ajustée
        BigDecimal adjustedPremium = premiumCalculator.calculateAdjustedPremium(basePremium, factors);

        // Récupération du coefficient de bonus-malus de la police
        BigDecimal bonusMalusCoefficient = policy.getBonusMalusCoefficient();

        // Calcul de la prime finale
        BigDecimal finalPremium = premiumCalculator.calculateFinalPremium(adjustedPremium, bonusMalusCoefficient);

        // Calcul des garanties
        Map<String, BigDecimal> coverages = premiumCalculator.calculateCoverages(finalPremium, policy.getCoverageType().name(), organizationId);

        // Construction du résultat
        return PremiumCalculationResultDTO.builder()
                .vehicleId(policy.getVehicleId())
                .driverId(policy.getPrimaryDriverId())
                .coverageType(policy.getCoverageType().name())
                .basePremium(basePremium)
                .bonusMalusCoefficient(bonusMalusCoefficient)
                .adjustedPremium(adjustedPremium)
                .finalPremium(finalPremium)
                .factors(factors)
                .coverages(coverages)
                .organizationId(organizationId)
                .build();
    }
}

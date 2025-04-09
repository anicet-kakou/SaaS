package com.devolution.saas.insurance.nonlife.auto.application.service.impl;

import com.devolution.saas.insurance.nonlife.auto.application.service.AutoPricingService;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.CoverageType;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Implémentation du service de tarification auto.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AutoPricingServiceImpl implements AutoPricingService {

    // Constantes pour la tarification
    private static final BigDecimal BASE_PREMIUM = new BigDecimal("500.00");
    private static final BigDecimal COMPREHENSIVE_MULTIPLIER = new BigDecimal("1.5");
    private static final BigDecimal THIRD_PARTY_MULTIPLIER = new BigDecimal("1.0");
    private static final BigDecimal ANTI_THEFT_DISCOUNT = new BigDecimal("0.95");
    private static final BigDecimal GARAGE_PARKING_DISCOUNT = new BigDecimal("0.90");
    private static final BigDecimal HIGH_VALUE_SURCHARGE = new BigDecimal("1.2");
    private static final BigDecimal HIGH_POWER_SURCHARGE = new BigDecimal("1.3");
    private static final BigDecimal GLASS_COVERAGE_PRICE = new BigDecimal("80.00");
    private static final BigDecimal ASSISTANCE_COVERAGE_PRICE = new BigDecimal("120.00");
    private static final BigDecimal DRIVER_COVERAGE_PRICE = new BigDecimal("150.00");
    private final VehicleRepository vehicleRepository;

    @Override
    public BigDecimal calculateBasePremium(UUID vehicleId, UUID organizationId) {
        log.debug("Calcul de la prime de base pour le véhicule: {}", vehicleId);

        // Récupération du véhicule
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (vehicleOpt.isEmpty()) {
            log.error("Véhicule non trouvé: {}", vehicleId);
            throw new IllegalArgumentException("Véhicule non trouvé");
        }

        Vehicle vehicle = vehicleOpt.get();

        // Vérification de l'organisation
        if (!vehicle.getOrganizationId().equals(organizationId)) {
            log.error("Le véhicule n'appartient pas à l'organisation: {}", organizationId);
            throw new IllegalArgumentException("Le véhicule n'appartient pas à l'organisation spécifiée");
        }

        // Calcul de la prime de base en fonction des caractéristiques du véhicule
        BigDecimal premium = BASE_PREMIUM;

        // Ajustement en fonction de la valeur du véhicule
        if (vehicle.getCurrentValue() != null && vehicle.getCurrentValue().compareTo(new BigDecimal("30000")) > 0) {
            premium = premium.multiply(HIGH_VALUE_SURCHARGE);
        }

        // Ajustement en fonction de la puissance du moteur
        if (vehicle.getEnginePower() != null && vehicle.getEnginePower() > 150) {
            premium = premium.multiply(HIGH_POWER_SURCHARGE);
        }

        // Ajustement en fonction de l'âge du véhicule
        int vehicleAge = java.time.LocalDate.now().getYear() - vehicle.getYear();
        if (vehicleAge > 10) {
            // Réduction pour les véhicules anciens
            premium = premium.multiply(new BigDecimal("0.8"));
        } else if (vehicleAge < 2) {
            // Majoration pour les véhicules neufs
            premium = premium.multiply(new BigDecimal("1.1"));
        }

        return premium.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateFinalPremium(UUID vehicleId, String coverageType, BigDecimal bonusMalusCoefficient,
                                            Map<String, Boolean> additionalOptions, UUID organizationId) {
        log.debug("Calcul de la prime finale pour le véhicule: {}, couverture: {}", vehicleId, coverageType);

        // Calcul de la prime de base
        BigDecimal basePremium = calculateBasePremium(vehicleId, organizationId);

        // Application du multiplicateur selon le type de couverture
        BigDecimal premium;
        if (CoverageType.COMPREHENSIVE.name().equals(coverageType)) {
            premium = basePremium.multiply(COMPREHENSIVE_MULTIPLIER);
        } else {
            premium = basePremium.multiply(THIRD_PARTY_MULTIPLIER);
        }

        // Application du coefficient bonus-malus
        if (bonusMalusCoefficient != null) {
            premium = premium.multiply(bonusMalusCoefficient);
        }

        // Application des options additionnelles
        if (additionalOptions != null) {
            if (Boolean.TRUE.equals(additionalOptions.get("antiTheftDevice"))) {
                premium = premium.multiply(ANTI_THEFT_DISCOUNT);
            }

            if (Boolean.TRUE.equals(additionalOptions.get("garageParking"))) {
                premium = premium.multiply(GARAGE_PARKING_DISCOUNT);
            }

            if (Boolean.TRUE.equals(additionalOptions.get("glassCoverage"))) {
                premium = premium.add(GLASS_COVERAGE_PRICE);
            }

            if (Boolean.TRUE.equals(additionalOptions.get("assistanceCoverage"))) {
                premium = premium.add(ASSISTANCE_COVERAGE_PRICE);
            }

            if (Boolean.TRUE.equals(additionalOptions.get("driverCoverage"))) {
                premium = premium.add(DRIVER_COVERAGE_PRICE);
            }
        }

        return premium.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public Map<String, BigDecimal> simulatePricingOptions(UUID vehicleId, UUID organizationId) {
        log.debug("Simulation des options de tarification pour le véhicule: {}", vehicleId);

        Map<String, BigDecimal> options = new HashMap<>();

        // Calcul des primes pour différentes options
        BigDecimal basePremium = calculateBasePremium(vehicleId, organizationId);
        options.put("basePremium", basePremium);

        // Option tiers
        BigDecimal thirdPartyPremium = basePremium.multiply(THIRD_PARTY_MULTIPLIER);
        options.put("thirdParty", thirdPartyPremium);

        // Option tous risques
        BigDecimal comprehensivePremium = basePremium.multiply(COMPREHENSIVE_MULTIPLIER);
        options.put("comprehensive", comprehensivePremium);

        // Option tous risques avec franchise réduite
        BigDecimal comprehensiveReducedDeductible = comprehensivePremium.multiply(new BigDecimal("1.15"));
        options.put("comprehensiveReducedDeductible", comprehensiveReducedDeductible);

        // Option tous risques avec garantie valeur à neuf
        BigDecimal comprehensiveNewValue = comprehensivePremium.multiply(new BigDecimal("1.25"));
        options.put("comprehensiveNewValue", comprehensiveNewValue);

        // Option bris de glace
        options.put("glassCoverage", GLASS_COVERAGE_PRICE);

        // Option assistance
        options.put("assistanceCoverage", ASSISTANCE_COVERAGE_PRICE);

        // Option protection du conducteur
        options.put("driverCoverage", DRIVER_COVERAGE_PRICE);

        return options;
    }
}

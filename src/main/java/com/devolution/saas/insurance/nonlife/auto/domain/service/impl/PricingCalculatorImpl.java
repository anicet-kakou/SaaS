package com.devolution.saas.insurance.nonlife.auto.domain.service.impl;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;
import com.devolution.saas.insurance.nonlife.auto.domain.port.VehicleCategoryProvider;
import com.devolution.saas.insurance.nonlife.auto.domain.port.VehicleUsageProvider;
import com.devolution.saas.insurance.nonlife.auto.domain.service.PricingCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implémentation du service de domaine pour le calcul des prix d'assurance auto.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PricingCalculatorImpl implements PricingCalculator {

    // Constantes pour le calcul des primes
    private static final BigDecimal BASE_PREMIUM = new BigDecimal("500.00");
    private static final BigDecimal COVERAGE_MULTIPLIER_THIRD_PARTY = new BigDecimal("1.0");
    private static final BigDecimal COVERAGE_MULTIPLIER_COMPREHENSIVE = new BigDecimal("2.0");
    private static final BigDecimal AGE_FACTOR_PER_YEAR = new BigDecimal("0.05");
    private static final BigDecimal ENGINE_POWER_FACTOR = new BigDecimal("0.02");
    private static final BigDecimal ANTI_THEFT_DISCOUNT = new BigDecimal("0.1");
    private final VehicleCategoryProvider vehicleCategoryProvider;
    private final VehicleUsageProvider vehicleUsageProvider;

    @Override
    public BigDecimal calculateBasePremium(Vehicle vehicle, AutoPolicy.CoverageType coverageType) {
        log.debug("Calcul de la prime de base pour le véhicule {} avec couverture {}",
                vehicle.getRegistrationNumber(), coverageType);

        // Prime de base
        BigDecimal premium = BASE_PREMIUM;

        // Facteur lié à la catégorie du véhicule
        BigDecimal categoryFactor = vehicleCategoryProvider.getCategoryRiskFactor(vehicle.getCategoryId());
        premium = premium.multiply(categoryFactor);

        // Facteur lié à l'usage du véhicule
        BigDecimal usageFactor = vehicleUsageProvider.getUsageRiskFactor(vehicle.getUsageId());
        premium = premium.multiply(usageFactor);

        // Facteur lié à l'âge du véhicule
        int vehicleAge = calculateVehicleAge(vehicle);
        BigDecimal ageFactor = BigDecimal.ONE.add(AGE_FACTOR_PER_YEAR.multiply(new BigDecimal(vehicleAge)));
        premium = premium.multiply(ageFactor);

        // Facteur lié à la puissance du moteur
        if (vehicle.getEnginePower() != null) {
            BigDecimal powerFactor = BigDecimal.ONE.add(
                    ENGINE_POWER_FACTOR.multiply(new BigDecimal(vehicle.getEnginePower())));
            premium = premium.multiply(powerFactor);
        }

        // Réduction pour dispositif antivol
        if (vehicle.isHasAntiTheftDevice()) {
            premium = premium.multiply(BigDecimal.ONE.subtract(ANTI_THEFT_DISCOUNT));
        }

        // Facteur lié au type de couverture
        BigDecimal coverageMultiplier = getCoverageMultiplier(coverageType);
        premium = premium.multiply(coverageMultiplier);

        return premium.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateFinalPremium(BigDecimal basePremium, BigDecimal bonusMalusCoefficient) {
        log.debug("Application du coefficient bonus-malus {} à la prime de base {}",
                bonusMalusCoefficient, basePremium);

        return basePremium.multiply(bonusMalusCoefficient)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTotalPremium(Vehicle vehicle, AutoPolicy.CoverageType coverageType,
                                            BigDecimal bonusMalusCoefficient) {
        BigDecimal basePremium = calculateBasePremium(vehicle, coverageType);
        return calculateFinalPremium(basePremium, bonusMalusCoefficient);
    }

    /**
     * Calcule l'âge du véhicule en années.
     *
     * @param vehicle Le véhicule
     * @return L'âge du véhicule en années
     */
    private int calculateVehicleAge(Vehicle vehicle) {
        int currentYear = java.time.Year.now().getValue();
        return currentYear - vehicle.getYear();
    }

    /**
     * Obtient le multiplicateur de prime en fonction du type de couverture.
     *
     * @param coverageType Le type de couverture
     * @return Le multiplicateur de prime
     */
    private BigDecimal getCoverageMultiplier(AutoPolicy.CoverageType coverageType) {
        switch (coverageType) {
            case THIRD_PARTY:
                return COVERAGE_MULTIPLIER_THIRD_PARTY;
            case COMPREHENSIVE:
                return COVERAGE_MULTIPLIER_COMPREHENSIVE;
            default:
                return BigDecimal.ONE;
        }
    }
}

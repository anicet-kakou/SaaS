package com.devolution.saas.insurance.nonlife.auto.domain.service;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;

import java.math.BigDecimal;

/**
 * Service de domaine pour le calcul des prix d'assurance auto.
 * Ce service contient la logique métier pure pour le calcul des primes.
 */
public interface PricingCalculator {

    /**
     * Calcule la prime de base pour un véhicule.
     *
     * @param vehicle      Le véhicule
     * @param coverageType Le type de couverture
     * @return La prime de base
     */
    BigDecimal calculateBasePremium(Vehicle vehicle, AutoPolicy.CoverageType coverageType);

    /**
     * Calcule la prime finale en appliquant le coefficient bonus-malus.
     *
     * @param basePremium           La prime de base
     * @param bonusMalusCoefficient Le coefficient bonus-malus
     * @return La prime finale
     */
    BigDecimal calculateFinalPremium(BigDecimal basePremium, BigDecimal bonusMalusCoefficient);

    /**
     * Calcule la prime totale pour une police d'assurance.
     *
     * @param vehicle               Le véhicule
     * @param coverageType          Le type de couverture
     * @param bonusMalusCoefficient Le coefficient bonus-malus
     * @return La prime totale
     */
    BigDecimal calculateTotalPremium(Vehicle vehicle, AutoPolicy.CoverageType coverageType, BigDecimal bonusMalusCoefficient);
}

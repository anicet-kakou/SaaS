package com.devolution.saas.insurance.nonlife.auto.domain.service;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Driver;
import com.devolution.saas.insurance.nonlife.auto.domain.model.Vehicle;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Service de calcul de la prime d'assurance auto.
 */
public interface PremiumCalculator {

    /**
     * Calcule la prime de base pour un véhicule et un type de couverture donnés.
     *
     * @param vehicle        Le véhicule
     * @param coverageType   Le type de couverture
     * @param organizationId L'ID de l'organisation
     * @return La prime de base
     */
    BigDecimal calculateBasePremium(Vehicle vehicle, String coverageType, UUID organizationId);

    /**
     * Calcule les facteurs d'ajustement pour un véhicule et un conducteur donnés.
     *
     * @param vehicle        Le véhicule
     * @param driver         Le conducteur
     * @param coverageType   Le type de couverture
     * @param organizationId L'ID de l'organisation
     * @return Les facteurs d'ajustement
     */
    Map<String, BigDecimal> calculateFactors(Vehicle vehicle, Driver driver, String coverageType, UUID organizationId);

    /**
     * Calcule la prime ajustée en appliquant les facteurs à la prime de base.
     *
     * @param basePremium La prime de base
     * @param factors     Les facteurs d'ajustement
     * @return La prime ajustée
     */
    BigDecimal calculateAdjustedPremium(BigDecimal basePremium, Map<String, BigDecimal> factors);

    /**
     * Calcule la prime finale en appliquant le coefficient de bonus-malus à la prime ajustée.
     *
     * @param adjustedPremium       La prime ajustée
     * @param bonusMalusCoefficient Le coefficient de bonus-malus
     * @return La prime finale
     */
    BigDecimal calculateFinalPremium(BigDecimal adjustedPremium, BigDecimal bonusMalusCoefficient);

    /**
     * Calcule les primes pour chaque garantie.
     *
     * @param finalPremium   La prime finale
     * @param coverageType   Le type de couverture
     * @param organizationId L'ID de l'organisation
     * @return Les primes par garantie
     */
    Map<String, BigDecimal> calculateCoverages(BigDecimal finalPremium, String coverageType, UUID organizationId);

    /**
     * Calcule la prime pour une police existante.
     *
     * @param policy         La police
     * @param vehicle        Le véhicule
     * @param driver         Le conducteur
     * @param organizationId L'ID de l'organisation
     * @return La prime calculée
     */
    BigDecimal calculatePolicyPremium(AutoPolicy policy, Vehicle vehicle, Driver driver, UUID organizationId);
}

package com.devolution.saas.insurance.nonlife.auto.application.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Service d'application pour la tarification auto.
 */
public interface AutoPricingService {

    /**
     * Calcule la prime de base pour un véhicule.
     *
     * @param vehicleId      L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     * @return La prime de base
     */
    BigDecimal calculateBasePremium(UUID vehicleId, UUID organizationId);

    /**
     * Calcule la prime finale avec toutes les majorations et réductions.
     *
     * @param vehicleId             L'ID du véhicule
     * @param coverageType          Le type de couverture (tiers ou tous risques)
     * @param bonusMalusCoefficient Le coefficient de bonus-malus
     * @param additionalOptions     Les options additionnelles (garanties optionnelles)
     * @param organizationId        L'ID de l'organisation
     * @return La prime finale
     */
    BigDecimal calculateFinalPremium(UUID vehicleId, String coverageType, BigDecimal bonusMalusCoefficient,
                                     Map<String, Boolean> additionalOptions, UUID organizationId);

    /**
     * Simule différentes options de tarification.
     *
     * @param vehicleId      L'ID du véhicule
     * @param organizationId L'ID de l'organisation
     * @return Une carte des différentes options de tarification
     */
    Map<String, BigDecimal> simulatePricingOptions(UUID vehicleId, UUID organizationId);
}

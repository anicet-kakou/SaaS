package com.devolution.saas.insurance.nonlife.auto.application.usecase;

import com.devolution.saas.insurance.nonlife.auto.application.dto.PremiumCalculationResultDTO;

import java.util.UUID;

/**
 * Cas d'utilisation pour le calcul de la prime d'assurance auto.
 */
public interface CalculateAutoPremium {

    /**
     * Calcule la prime d'assurance auto pour un véhicule et un conducteur donnés.
     *
     * @param vehicleId L'ID du véhicule
     * @param driverId L'ID du conducteur
     * @param coverageType Le type de couverture (THIRD_PARTY, COMPREHENSIVE)
     * @param organizationId L'ID de l'organisation
     * @return Le résultat du calcul de la prime
     */
    PremiumCalculationResultDTO calculate(UUID vehicleId, UUID driverId, String coverageType, UUID organizationId);

    /**
     * Calcule la prime d'assurance auto pour une police existante.
     *
     * @param policyId       L'ID de la police
     * @param organizationId L'ID de l'organisation
     * @return Le résultat du calcul de la prime
     */
    PremiumCalculationResultDTO calculateForPolicy(UUID policyId, UUID organizationId);
}

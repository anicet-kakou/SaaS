package com.devolution.saas.insurance.nonlife.auto.application.usecase;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Cas d'utilisation pour le calcul de prime auto.
 */
public interface CalculateAutoPremium {

    /**
     * Calcule la prime d'assurance auto.
     *
     * @param vehicleId             L'ID du véhicule
     * @param coverageType          Le type de couverture (tiers ou tous risques)
     * @param bonusMalusCoefficient Le coefficient de bonus-malus
     * @param organizationId        L'ID de l'organisation
     * @return Le montant de la prime
     */
    BigDecimal execute(UUID vehicleId, String coverageType, BigDecimal bonusMalusCoefficient, UUID organizationId);
}

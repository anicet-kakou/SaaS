package com.devolution.saas.insurance.nonlife.auto.domain.service;

import java.math.BigDecimal;

/**
 * Service de domaine pour le calcul du bonus-malus.
 */
public interface BonusMalusCalculator {

    /**
     * Calcule le nouveau coefficient de bonus-malus.
     *
     * @param currentCoefficient Le coefficient actuel
     * @param claimCount         Le nombre de sinistres responsables
     * @return Le nouveau coefficient
     */
    BigDecimal calculateNewCoefficient(BigDecimal currentCoefficient, int claimCount);

    /**
     * Applique la réduction annuelle pour absence de sinistre.
     *
     * @param currentCoefficient Le coefficient actuel
     * @return Le nouveau coefficient après réduction
     */
    BigDecimal applyAnnualReduction(BigDecimal currentCoefficient);

    /**
     * Applique la majoration pour sinistre responsable.
     *
     * @param currentCoefficient Le coefficient actuel
     * @return Le nouveau coefficient après majoration
     */
    BigDecimal applySurchargeForClaim(BigDecimal currentCoefficient);
}

package com.devolution.saas.insurance.nonlife.auto.domain.model;

import java.math.BigDecimal;

/**
 * Constantes liées au bonus-malus.
 */
public final class BonusMalusConstants {

    /**
     * Coefficient minimum de bonus-malus.
     */
    public static final BigDecimal MIN_COEFFICIENT = new BigDecimal("0.50");
    /**
     * Coefficient maximum de bonus-malus.
     */
    public static final BigDecimal MAX_COEFFICIENT = new BigDecimal("3.50");
    /**
     * Coefficient initial de bonus-malus.
     */
    public static final BigDecimal INITIAL_COEFFICIENT = new BigDecimal("1.00");

    private BonusMalusConstants() {
        // Constructeur privé pour empêcher l'instanciation
    }
}

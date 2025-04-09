package com.devolution.saas.insurance.nonlife.auto.domain.service.impl;

import com.devolution.saas.insurance.nonlife.auto.domain.service.BonusMalusCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implémentation du calculateur de bonus-malus.
 */
@Service
public class BonusMalusCalculatorImpl implements BonusMalusCalculator {

    private static final BigDecimal MIN_COEFFICIENT = new BigDecimal("0.50");
    private static final BigDecimal MAX_COEFFICIENT = new BigDecimal("3.50");
    private static final BigDecimal ANNUAL_REDUCTION_RATE = new BigDecimal("0.05");
    private static final BigDecimal CLAIM_SURCHARGE_RATE = new BigDecimal("0.25");

    @Override
    public BigDecimal calculateNewCoefficient(BigDecimal currentCoefficient, int claimCount) {
        if (claimCount == 0) {
            return applyAnnualReduction(currentCoefficient);
        } else {
            BigDecimal newCoefficient = currentCoefficient;
            for (int i = 0; i < claimCount; i++) {
                newCoefficient = applySurchargeForClaim(newCoefficient);
            }
            return newCoefficient;
        }
    }

    @Override
    public BigDecimal applyAnnualReduction(BigDecimal currentCoefficient) {
        BigDecimal reduction = currentCoefficient.multiply(ANNUAL_REDUCTION_RATE);
        BigDecimal newCoefficient = currentCoefficient.subtract(reduction);

        // Ne pas descendre en dessous du coefficient minimum
        return newCoefficient.compareTo(MIN_COEFFICIENT) < 0 ? MIN_COEFFICIENT : newCoefficient.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal applySurchargeForClaim(BigDecimal currentCoefficient) {
        BigDecimal surcharge = currentCoefficient.multiply(CLAIM_SURCHARGE_RATE);
        BigDecimal newCoefficient = currentCoefficient.add(surcharge);

        // Ne pas dépasser le coefficient maximum
        return newCoefficient.compareTo(MAX_COEFFICIENT) > 0 ? MAX_COEFFICIENT : newCoefficient.setScale(2, RoundingMode.HALF_UP);
    }
}

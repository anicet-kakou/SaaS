package com.devolution.saas.insurance.nonlife.auto.application.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * DTO pour le résultat du calcul de la prime d'assurance auto.
 */
public record PremiumCalculationResultDTO(
        /**
         * ID du véhicule.
         */
        UUID vehicleId,

        /**
         * ID du conducteur.
         */
        UUID driverId,

        /**
         * Type de couverture.
         */
        String coverageType,

        /**
         * Montant de la prime de base.
         */
        BigDecimal basePremium,

        /**
         * Coefficient de bonus-malus.
         */
        BigDecimal bonusMalusCoefficient,

        /**
         * Montant de la prime après application du bonus-malus.
         */
        BigDecimal adjustedPremium,

        /**
         * Montant de la prime finale.
         */
        BigDecimal finalPremium,

        /**
         * Détails des facteurs de calcul.
         */
        Map<String, BigDecimal> factors,

        /**
         * Détails des garanties et leurs primes.
         */
        Map<String, BigDecimal> coverages,

        /**
         * ID de l'organisation.
         */
        UUID organizationId
) {
    /**
     * Builder for PremiumCalculationResultDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for PremiumCalculationResultDTO.
     */
    public static class Builder {
        private UUID vehicleId;
        private UUID driverId;
        private String coverageType;
        private BigDecimal basePremium;
        private BigDecimal bonusMalusCoefficient;
        private BigDecimal adjustedPremium;
        private BigDecimal finalPremium;
        private Map<String, BigDecimal> factors;
        private Map<String, BigDecimal> coverages;
        private UUID organizationId;

        public Builder vehicleId(UUID vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public Builder driverId(UUID driverId) {
            this.driverId = driverId;
            return this;
        }

        public Builder coverageType(String coverageType) {
            this.coverageType = coverageType;
            return this;
        }

        public Builder basePremium(BigDecimal basePremium) {
            this.basePremium = basePremium;
            return this;
        }

        public Builder bonusMalusCoefficient(BigDecimal bonusMalusCoefficient) {
            this.bonusMalusCoefficient = bonusMalusCoefficient;
            return this;
        }

        public Builder adjustedPremium(BigDecimal adjustedPremium) {
            this.adjustedPremium = adjustedPremium;
            return this;
        }

        public Builder finalPremium(BigDecimal finalPremium) {
            this.finalPremium = finalPremium;
            return this;
        }

        public Builder factors(Map<String, BigDecimal> factors) {
            this.factors = factors;
            return this;
        }

        public Builder coverages(Map<String, BigDecimal> coverages) {
            this.coverages = coverages;
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public PremiumCalculationResultDTO build() {
            return new PremiumCalculationResultDTO(
                    vehicleId, driverId, coverageType, basePremium, bonusMalusCoefficient,
                    adjustedPremium, finalPremium, factors, coverages, organizationId
            );
        }
    }
}

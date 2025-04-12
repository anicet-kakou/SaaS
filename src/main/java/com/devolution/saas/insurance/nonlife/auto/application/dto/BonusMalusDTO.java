package com.devolution.saas.insurance.nonlife.auto.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO pour le bonus-malus.
 */
public record BonusMalusDTO(
        UUID id,
        UUID customerId,
        String customerName,
        BigDecimal coefficient,
        LocalDate effectiveDate,
        LocalDate expiryDate,
        BigDecimal previousCoefficient,
        int yearsWithoutClaim
) {
    /**
     * Builder for BonusMalusDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for BonusMalusDTO.
     */
    public static class Builder {
        private UUID id;
        private UUID customerId;
        private String customerName;
        private BigDecimal coefficient;
        private LocalDate effectiveDate;
        private LocalDate expiryDate;
        private BigDecimal previousCoefficient;
        private int yearsWithoutClaim;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder customerId(UUID customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder coefficient(BigDecimal coefficient) {
            this.coefficient = coefficient;
            return this;
        }

        public Builder effectiveDate(LocalDate effectiveDate) {
            this.effectiveDate = effectiveDate;
            return this;
        }

        public Builder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder previousCoefficient(BigDecimal previousCoefficient) {
            this.previousCoefficient = previousCoefficient;
            return this;
        }

        public Builder yearsWithoutClaim(int yearsWithoutClaim) {
            this.yearsWithoutClaim = yearsWithoutClaim;
            return this;
        }

        public BonusMalusDTO build() {
            return new BonusMalusDTO(
                    id, customerId, customerName, coefficient, effectiveDate, expiryDate,
                    previousCoefficient, yearsWithoutClaim
            );
        }
    }
}

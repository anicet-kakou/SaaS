package com.devolution.saas.insurance.nonlife.auto.application.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO pour le contexte de calcul de police d'assurance auto.
 */
public record PolicyCalculationContextDTO(
        UUID vehicleId,
        UUID driverId,
        String coverageType,
        BigDecimal bonusMalusCoefficient,
        List<String> selectedCoverages,
        UUID organizationId
) {
    /**
     * Constructeur compact avec valeurs par défaut pour les collections.
     */
    public PolicyCalculationContextDTO {
        selectedCoverages = selectedCoverages != null ? selectedCoverages : new ArrayList<>();
    }

    /**
     * Builder pour PolicyCalculationContextDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour PolicyCalculationContextDTO.
     */
    public static class Builder {
        private UUID vehicleId;
        private UUID driverId;
        private String coverageType;
        private BigDecimal bonusMalusCoefficient;
        private List<String> selectedCoverages = new ArrayList<>();
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

        public Builder bonusMalusCoefficient(BigDecimal bonusMalusCoefficient) {
            this.bonusMalusCoefficient = bonusMalusCoefficient;
            return this;
        }

        public Builder selectedCoverages(List<String> selectedCoverages) {
            this.selectedCoverages = selectedCoverages != null ? selectedCoverages : new ArrayList<>();
            return this;
        }

        public Builder addSelectedCoverage(String coverage) {
            if (this.selectedCoverages == null) {
                this.selectedCoverages = new ArrayList<>();
            }
            this.selectedCoverages.add(coverage);
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public PolicyCalculationContextDTO build() {
            return new PolicyCalculationContextDTO(
                    vehicleId, driverId, coverageType, bonusMalusCoefficient, selectedCoverages, organizationId
            );
        }
    }
}

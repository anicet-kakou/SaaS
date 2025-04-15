package com.devolution.saas.insurance.nonlife.auto.application.command;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.CoverageType;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.ParkingType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Commande pour la création d'une police d'assurance automobile.
 */
public record CreateAutoPolicyCommand(
    /**
     * Numéro de police.
     */
    String policyNumber,

    /**
     * Date de début de la police.
     */
    LocalDate startDate,

    /**
     * Date de fin de la police.
     */
    LocalDate endDate,

    /**
     * ID du véhicule assuré.
     */
    UUID vehicleId,

    /**
     * ID du conducteur principal.
     */
    UUID primaryDriverId,

    /**
     * Type de couverture.
     */
    CoverageType coverageType,

    /**
     * Coefficient de bonus-malus.
     */
    BigDecimal bonusMalusCoefficient,

    /**
     * Kilométrage annuel.
     */
    Integer annualMileage,

    /**
     * Type de stationnement.
     */
    ParkingType parkingType,

    /**
     * Indique si le véhicule est équipé d'un dispositif antivol.
     */
    boolean hasAntiTheftDevice,

    /**
     * ID de la catégorie d'historique de sinistres.
     */
    UUID claimHistoryCategoryId,

    /**
     * ID de l'organisation.
     */
    UUID organizationId
) {
    /**
     * Builder pour CreateAutoPolicyCommand.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour CreateAutoPolicyCommand.
     */
    public static class Builder {
        private String policyNumber;
        private LocalDate startDate;
        private LocalDate endDate;
        private UUID vehicleId;
        private UUID primaryDriverId;
        private CoverageType coverageType;
        private BigDecimal bonusMalusCoefficient;
        private Integer annualMileage;
        private ParkingType parkingType;
        private boolean hasAntiTheftDevice;
        private UUID claimHistoryCategoryId;
        private UUID organizationId;

        public Builder policyNumber(String policyNumber) {
            this.policyNumber = policyNumber;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder vehicleId(UUID vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public Builder primaryDriverId(UUID primaryDriverId) {
            this.primaryDriverId = primaryDriverId;
            return this;
        }

        public Builder coverageType(CoverageType coverageType) {
            this.coverageType = coverageType;
            return this;
        }

        public Builder bonusMalusCoefficient(BigDecimal bonusMalusCoefficient) {
            this.bonusMalusCoefficient = bonusMalusCoefficient;
            return this;
        }

        public Builder annualMileage(Integer annualMileage) {
            this.annualMileage = annualMileage;
            return this;
        }

        public Builder parkingType(ParkingType parkingType) {
            this.parkingType = parkingType;
            return this;
        }

        public Builder hasAntiTheftDevice(boolean hasAntiTheftDevice) {
            this.hasAntiTheftDevice = hasAntiTheftDevice;
            return this;
        }

        public Builder claimHistoryCategoryId(UUID claimHistoryCategoryId) {
            this.claimHistoryCategoryId = claimHistoryCategoryId;
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public CreateAutoPolicyCommand build() {
            return new CreateAutoPolicyCommand(
                    policyNumber, startDate, endDate, vehicleId, primaryDriverId, coverageType,
                    bonusMalusCoefficient, annualMileage, parkingType, hasAntiTheftDevice,
                    claimHistoryCategoryId, organizationId);
        }
    }
}

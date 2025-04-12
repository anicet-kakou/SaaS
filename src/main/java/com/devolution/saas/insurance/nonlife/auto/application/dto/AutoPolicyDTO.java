package com.devolution.saas.insurance.nonlife.auto.application.dto;

import com.devolution.saas.insurance.common.domain.model.Policy.PolicyStatus;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.CoverageType;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.ParkingType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO pour les polices d'assurance automobile.
 */
public record AutoPolicyDTO(
        UUID id,
        String policyNumber,
        PolicyStatus status,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal premiumAmount,
        UUID vehicleId,
        String vehicleRegistrationNumber,
        UUID primaryDriverId,
        String primaryDriverName,
        CoverageType coverageType,
        BigDecimal bonusMalusCoefficient,
        Integer annualMileage,
        ParkingType parkingType,
        boolean hasAntiTheftDevice,
        UUID claimHistoryCategoryId,
        UUID organizationId
) {
    /**
     * Builder for AutoPolicyDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for AutoPolicyDTO.
     */
    public static class Builder {
        private UUID id;
        private String policyNumber;
        private PolicyStatus status;
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal premiumAmount;
        private UUID vehicleId;
        private String vehicleRegistrationNumber;
        private UUID primaryDriverId;
        private String primaryDriverName;
        private CoverageType coverageType;
        private BigDecimal bonusMalusCoefficient;
        private Integer annualMileage;
        private ParkingType parkingType;
        private boolean hasAntiTheftDevice;
        private UUID claimHistoryCategoryId;
        private UUID organizationId;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder policyNumber(String policyNumber) {
            this.policyNumber = policyNumber;
            return this;
        }

        public Builder status(PolicyStatus status) {
            this.status = status;
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

        public Builder premiumAmount(BigDecimal premiumAmount) {
            this.premiumAmount = premiumAmount;
            return this;
        }

        public Builder vehicleId(UUID vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }

        public Builder vehicleRegistrationNumber(String vehicleRegistrationNumber) {
            this.vehicleRegistrationNumber = vehicleRegistrationNumber;
            return this;
        }

        public Builder primaryDriverId(UUID primaryDriverId) {
            this.primaryDriverId = primaryDriverId;
            return this;
        }

        public Builder primaryDriverName(String primaryDriverName) {
            this.primaryDriverName = primaryDriverName;
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

        public AutoPolicyDTO build() {
            return new AutoPolicyDTO(
                    id, policyNumber, status, startDate, endDate, premiumAmount, vehicleId,
                    vehicleRegistrationNumber, primaryDriverId, primaryDriverName, coverageType,
                    bonusMalusCoefficient, annualMileage, parkingType, hasAntiTheftDevice,
                    claimHistoryCategoryId, organizationId
            );
        }
    }
}

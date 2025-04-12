package com.devolution.saas.insurance.nonlife.auto.application.dto;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO pour les conducteurs.
 */
public record DriverDTO(
        UUID id,
        UUID customerId,
        String customerName,
        String licenseNumber,
        UUID licenseTypeId,
        String licenseTypeName,
        LocalDate licenseIssueDate,
        LocalDate licenseExpiryDate,
        boolean isPrimaryDriver,
        int yearsOfDrivingExperience,
        UUID organizationId
) {
    /**
     * Builder for DriverDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for DriverDTO.
     */
    public static class Builder {
        private UUID id;
        private UUID customerId;
        private String customerName;
        private String licenseNumber;
        private UUID licenseTypeId;
        private String licenseTypeName;
        private LocalDate licenseIssueDate;
        private LocalDate licenseExpiryDate;
        private boolean isPrimaryDriver;
        private int yearsOfDrivingExperience;
        private UUID organizationId;

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

        public Builder licenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
            return this;
        }

        public Builder licenseTypeId(UUID licenseTypeId) {
            this.licenseTypeId = licenseTypeId;
            return this;
        }

        public Builder licenseTypeName(String licenseTypeName) {
            this.licenseTypeName = licenseTypeName;
            return this;
        }

        public Builder licenseIssueDate(LocalDate licenseIssueDate) {
            this.licenseIssueDate = licenseIssueDate;
            return this;
        }

        public Builder licenseExpiryDate(LocalDate licenseExpiryDate) {
            this.licenseExpiryDate = licenseExpiryDate;
            return this;
        }

        public Builder isPrimaryDriver(boolean isPrimaryDriver) {
            this.isPrimaryDriver = isPrimaryDriver;
            return this;
        }

        public Builder yearsOfDrivingExperience(int yearsOfDrivingExperience) {
            this.yearsOfDrivingExperience = yearsOfDrivingExperience;
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public DriverDTO build() {
            return new DriverDTO(
                    id, customerId, customerName, licenseNumber, licenseTypeId, licenseTypeName,
                    licenseIssueDate, licenseExpiryDate, isPrimaryDriver, yearsOfDrivingExperience,
                    organizationId
            );
        }
    }
}

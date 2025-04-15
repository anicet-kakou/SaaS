package com.devolution.saas.insurance.nonlife.auto.application.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Commande pour la création d'un véhicule.
 */
public record CreateVehicleCommand(
        String registrationNumber,
        UUID makeId,
        UUID modelId,
        String modelVariant,
        Long version,
        int year,
        Integer enginePower,
        Integer engineSize,
        UUID fuelTypeId,
        UUID categoryId,
        UUID subcategoryId,
        UUID usageId,
        UUID geographicZoneId,
        LocalDate purchaseDate,
        BigDecimal purchaseValue,
        BigDecimal currentValue,
        Integer mileage,
        String vin,
        UUID colorId,
        UUID ownerId,
        UUID organizationId
) {
    /**
     * Builder pour CreateVehicleCommand.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour CreateVehicleCommand.
     */
    public static class Builder {
        private String registrationNumber;
        private UUID makeId;
        private UUID modelId;
        private String modelVariant;
        private Long version;
        private int year;
        private Integer enginePower;
        private Integer engineSize;
        private UUID fuelTypeId;
        private UUID categoryId;
        private UUID subcategoryId;
        private UUID usageId;
        private UUID geographicZoneId;
        private LocalDate purchaseDate;
        private BigDecimal purchaseValue;
        private BigDecimal currentValue;
        private Integer mileage;
        private String vin;
        private UUID colorId;
        private UUID ownerId;
        private UUID organizationId;

        public Builder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public Builder makeId(UUID makeId) {
            this.makeId = makeId;
            return this;
        }

        public Builder modelId(UUID modelId) {
            this.modelId = modelId;
            return this;
        }

        public Builder modelVariant(String modelVariant) {
            this.modelVariant = modelVariant;
            return this;
        }

        public Builder version(Long version) {
            this.version = version;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder enginePower(Integer enginePower) {
            this.enginePower = enginePower;
            return this;
        }

        public Builder engineSize(Integer engineSize) {
            this.engineSize = engineSize;
            return this;
        }

        public Builder fuelTypeId(UUID fuelTypeId) {
            this.fuelTypeId = fuelTypeId;
            return this;
        }

        public Builder categoryId(UUID categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder subcategoryId(UUID subcategoryId) {
            this.subcategoryId = subcategoryId;
            return this;
        }

        public Builder usageId(UUID usageId) {
            this.usageId = usageId;
            return this;
        }

        public Builder geographicZoneId(UUID geographicZoneId) {
            this.geographicZoneId = geographicZoneId;
            return this;
        }

        public Builder purchaseDate(LocalDate purchaseDate) {
            this.purchaseDate = purchaseDate;
            return this;
        }

        public Builder purchaseValue(BigDecimal purchaseValue) {
            this.purchaseValue = purchaseValue;
            return this;
        }

        public Builder currentValue(BigDecimal currentValue) {
            this.currentValue = currentValue;
            return this;
        }

        public Builder mileage(Integer mileage) {
            this.mileage = mileage;
            return this;
        }

        public Builder vin(String vin) {
            this.vin = vin;
            return this;
        }

        public Builder colorId(UUID colorId) {
            this.colorId = colorId;
            return this;
        }

        public Builder ownerId(UUID ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public CreateVehicleCommand build() {
            return new CreateVehicleCommand(
                    registrationNumber, makeId, modelId, modelVariant, version, year, enginePower,
                    engineSize, fuelTypeId, categoryId, subcategoryId, usageId, geographicZoneId,
                    purchaseDate, purchaseValue, currentValue, mileage, vin, colorId, ownerId, organizationId);
        }
    }
}

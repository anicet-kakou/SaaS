package com.devolution.saas.insurance.nonlife.auto.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO pour les véhicules.
 */
public record VehicleDTO(
        /**
         * ID du véhicule.
         */
        UUID id,

        /**
         * Numéro d'immatriculation du véhicule.
         */
        @NotBlank(message = "Le numéro d'immatriculation est obligatoire")
        @Size(min = 2, max = 20, message = "Le numéro d'immatriculation doit contenir entre 2 et 20 caractères")
        @Pattern(regexp = "^[A-Z0-9-]+$", message = "Le numéro d'immatriculation ne doit contenir que des lettres majuscules, des chiffres et des tirets")
        String registrationNumber,

        /**
         * ID du fabricant du véhicule.
         */
        @NotNull(message = "L'ID du fabricant est obligatoire")
        UUID manufacturerId,

        /**
         * Nom du fabricant du véhicule.
         */
        String manufacturerName,

        /**
         * ID du modèle du véhicule.
         */
        @NotNull(message = "L'ID du modèle est obligatoire")
        UUID modelId,

        /**
         * Nom du modèle du véhicule.
         */
        String modelName,

        /**
         * Variante du modèle du véhicule.
         */
        String modelVariant,

        /**
         * Version du véhicule (pour le contrôle de concurrence).
         */
        Long version,

        /**
         * Année de fabrication du véhicule.
         */
        @NotNull(message = "L'année de fabrication est obligatoire")
        int year,

        /**
         * Puissance du moteur en chevaux.
         */
        Integer enginePower,

        /**
         * Cylindrée du moteur en cm³.
         */
        Integer engineSize,

        /**
         * ID du type de carburant.
         */
        UUID fuelTypeId,

        /**
         * Nom du type de carburant.
         */
        String fuelTypeName,

        /**
         * ID de la catégorie du véhicule.
         */
        UUID categoryId,

        /**
         * Nom de la catégorie du véhicule.
         */
        String categoryName,

        /**
         * ID de la sous-catégorie du véhicule.
         */
        UUID subcategoryId,

        /**
         * Nom de la sous-catégorie du véhicule.
         */
        String subcategoryName,

        /**
         * ID de l'usage du véhicule.
         */
        UUID usageId,

        /**
         * Nom de l'usage du véhicule.
         */
        String usageName,

        /**
         * ID de la zone géographique.
         */
        UUID geographicZoneId,

        /**
         * Nom de la zone géographique.
         */
        String geographicZoneName,

        /**
         * Date d'achat du véhicule.
         */
        LocalDate purchaseDate,

        /**
         * Valeur d'achat du véhicule.
         */
        BigDecimal purchaseValue,

        /**
         * Valeur actuelle du véhicule.
         */
        BigDecimal currentValue,

        /**
         * Kilométrage du véhicule.
         */
        Integer mileage,

        /**
         * Numéro d'identification du véhicule (VIN).
         */
        @Size(min = 17, max = 17, message = "Le numéro VIN doit contenir exactement 17 caractères")
        @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Le numéro VIN n'est pas valide")
        String vin,

        /**
         * ID de la couleur du véhicule.
         */
        UUID colorId,

        /**
         * Nom de la couleur du véhicule.
         */
        String colorName,

        /**
         * ID du propriétaire du véhicule.
         */
        UUID ownerId,

        /**
         * Nom du propriétaire du véhicule.
         */
        String ownerName,

        /**
         * ID de l'organisation.
         */
        UUID organizationId,

        /**
         * ID de la police d'assurance auto associée.
         */
        UUID autoPolicyId,

        /**
         * Indique si le véhicule est équipé d'un dispositif antivol.
         */
        boolean hasAntiTheftDevice,

        /**
         * Type de stationnement du véhicule.
         */
        String parkingType
) {
    /**
     * Builder for VehicleDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for VehicleDTO.
     */
    public static class Builder {
        private UUID id;
        private String registrationNumber;
        private UUID manufacturerId;
        private String manufacturerName;
        private UUID modelId;
        private String modelName;
        private String modelVariant;
        private Long version;
        private int year;
        private Integer enginePower;
        private Integer engineSize;
        private UUID fuelTypeId;
        private String fuelTypeName;
        private UUID categoryId;
        private String categoryName;
        private UUID subcategoryId;
        private String subcategoryName;
        private UUID usageId;
        private String usageName;
        private UUID geographicZoneId;
        private String geographicZoneName;
        private LocalDate purchaseDate;
        private BigDecimal purchaseValue;
        private BigDecimal currentValue;
        private Integer mileage;
        private String vin;
        private UUID colorId;
        private String colorName;
        private UUID ownerId;
        private String ownerName;
        private UUID organizationId;
        private UUID autoPolicyId;
        private boolean hasAntiTheftDevice;
        private String parkingType;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public Builder manufacturerId(UUID manufacturerId) {
            this.manufacturerId = manufacturerId;
            return this;
        }

        public Builder manufacturerName(String manufacturerName) {
            this.manufacturerName = manufacturerName;
            return this;
        }

        public Builder modelId(UUID modelId) {
            this.modelId = modelId;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
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

        public Builder fuelTypeName(String fuelTypeName) {
            this.fuelTypeName = fuelTypeName;
            return this;
        }

        public Builder categoryId(UUID categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder categoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public Builder subcategoryId(UUID subcategoryId) {
            this.subcategoryId = subcategoryId;
            return this;
        }

        public Builder subcategoryName(String subcategoryName) {
            this.subcategoryName = subcategoryName;
            return this;
        }

        public Builder usageId(UUID usageId) {
            this.usageId = usageId;
            return this;
        }

        public Builder usageName(String usageName) {
            this.usageName = usageName;
            return this;
        }

        public Builder geographicZoneId(UUID geographicZoneId) {
            this.geographicZoneId = geographicZoneId;
            return this;
        }

        public Builder geographicZoneName(String geographicZoneName) {
            this.geographicZoneName = geographicZoneName;
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

        public Builder colorName(String colorName) {
            this.colorName = colorName;
            return this;
        }

        public Builder ownerId(UUID ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public Builder ownerName(String ownerName) {
            this.ownerName = ownerName;
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder autoPolicyId(UUID autoPolicyId) {
            this.autoPolicyId = autoPolicyId;
            return this;
        }

        public Builder hasAntiTheftDevice(boolean hasAntiTheftDevice) {
            this.hasAntiTheftDevice = hasAntiTheftDevice;
            return this;
        }

        public Builder parkingType(String parkingType) {
            this.parkingType = parkingType;
            return this;
        }

        public VehicleDTO build() {
            return new VehicleDTO(
                    id, registrationNumber, manufacturerId, manufacturerName, modelId, modelName, modelVariant, version,
                    year, enginePower, engineSize, fuelTypeId, fuelTypeName, categoryId, categoryName,
                    subcategoryId, subcategoryName, usageId, usageName, geographicZoneId, geographicZoneName,
                    purchaseDate, purchaseValue, currentValue, mileage, vin, colorId, colorName, ownerId,
                    ownerName, organizationId, autoPolicyId, hasAntiTheftDevice, parkingType
            );
        }
    }
}

package com.devolution.saas.insurance.nonlife.auto.api.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Requête pour la création d'un véhicule.
 */
public record CreateVehicleRequest(
        /**
         * Numéro d'immatriculation du véhicule.
         */
        @NotBlank(message = "Le numéro d'immatriculation est obligatoire")
        @Size(min = 2, max = 20, message = "Le numéro d'immatriculation doit contenir entre 2 et 20 caractères")
        @Pattern(regexp = "^[A-Z0-9-]+$", message = "Le numéro d'immatriculation ne doit contenir que des lettres majuscules, des chiffres et des tirets")
        String registrationNumber,

        /**
         * ID de la marque du véhicule.
         */
        @NotNull(message = "L'ID de la marque est obligatoire")
        UUID makeId,

        /**
         * ID du modèle du véhicule.
         */
        @NotNull(message = "L'ID du modèle est obligatoire")
        UUID modelId,

        /**
         * Version du véhicule.
         */
        @Size(max = 50, message = "La version ne doit pas dépasser 50 caractères")
        String version,

        /**
         * Année de fabrication du véhicule.
         */
        @NotNull(message = "L'année est obligatoire")
        @Min(value = 1900, message = "L'année doit être supérieure à 1900")
        Integer year,

        /**
         * Puissance du moteur en chevaux.
         */
        @Min(value = 1, message = "La puissance du moteur doit être supérieure à 0")
        Integer enginePower,

        /**
         * Cylindrée du moteur en cm3.
         */
        @Min(value = 1, message = "La cylindrée du moteur doit être supérieure à 0")
        Integer engineSize,

        /**
         * ID du type de carburant.
         */
        @NotNull(message = "L'ID du type de carburant est obligatoire")
        UUID fuelTypeId,

        /**
         * ID de la catégorie du véhicule.
         */
        @NotNull(message = "L'ID de la catégorie est obligatoire")
        UUID categoryId,

        /**
         * ID de la sous-catégorie du véhicule.
         */
        UUID subcategoryId,

        /**
         * ID du type d'usage du véhicule.
         */
        @NotNull(message = "L'ID du type d'usage est obligatoire")
        UUID usageId,

        /**
         * ID de la zone géographique.
         */
        @NotNull(message = "L'ID de la zone géographique est obligatoire")
        UUID geographicZoneId,

        /**
         * Date d'achat du véhicule.
         */
        LocalDate purchaseDate,

        /**
         * Valeur d'achat du véhicule.
         */
        @Min(value = 0, message = "La valeur d'achat doit être supérieure ou égale à 0")
        BigDecimal purchaseValue,

        /**
         * Valeur actuelle du véhicule.
         */
        @Min(value = 0, message = "La valeur actuelle doit être supérieure ou égale à 0")
        BigDecimal currentValue,

        /**
         * Kilométrage du véhicule.
         */
        @Min(value = 0, message = "Le kilométrage doit être supérieur ou égal à 0")
        Integer mileage,

        /**
         * Numéro d'identification du véhicule (VIN).
         */
        @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Le VIN doit contenir 17 caractères alphanumériques (sans I, O, Q)")
        String vin,

        /**
         * ID de la couleur du véhicule.
         */
        @NotNull(message = "L'ID de la couleur est obligatoire")
        UUID colorId,

        /**
         * ID du propriétaire du véhicule.
         */
        @NotNull(message = "L'ID du propriétaire est obligatoire")
        UUID ownerId
) {
    /**
     * Builder pour CreateVehicleRequest.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour CreateVehicleRequest.
     */
    public static class Builder {
        private String registrationNumber;
        private UUID makeId;
        private UUID modelId;
        private String version;
        private Integer year;
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

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder year(Integer year) {
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

        public CreateVehicleRequest build() {
            return new CreateVehicleRequest(
                    registrationNumber, makeId, modelId, version, year, enginePower, engineSize,
                    fuelTypeId, categoryId, subcategoryId, usageId, geographicZoneId, purchaseDate,
                    purchaseValue, currentValue, mileage, vin, colorId, ownerId
            );
        }
    }
}

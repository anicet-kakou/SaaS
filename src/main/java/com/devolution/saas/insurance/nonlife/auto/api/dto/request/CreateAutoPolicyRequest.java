package com.devolution.saas.insurance.nonlife.auto.api.dto.request;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.CoverageType;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.ParkingType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Requête pour la création d'une police d'assurance automobile.
 */
public record CreateAutoPolicyRequest(
        /**
         * Numéro de police.
         */
        @NotBlank(message = "Le numéro de police est obligatoire")
        @Size(min = 2, max = 50, message = "Le numéro de police doit contenir entre 2 et 50 caractères")
        @Pattern(regexp = "^[A-Z0-9-]+$", message = "Le numéro de police ne doit contenir que des lettres majuscules, des chiffres et des tirets")
        String policyNumber,

        /**
         * Date de début de la police.
         */
        @NotNull(message = "La date de début est obligatoire")
        @FutureOrPresent(message = "La date de début doit être aujourd'hui ou dans le futur")
        LocalDate startDate,

        /**
         * Date de fin de la police.
         */
        @NotNull(message = "La date de fin est obligatoire")
        @Future(message = "La date de fin doit être dans le futur")
        LocalDate endDate,

        /**
         * Montant de la prime.
         */
        @NotNull(message = "Le montant de la prime est obligatoire")
        @DecimalMin(value = "0.0", inclusive = false, message = "Le montant de la prime doit être supérieur à 0")
        @DecimalMax(value = "1000000.0", message = "Le montant de la prime doit être inférieur à 1 000 000")
        BigDecimal premiumAmount,

        /**
         * ID du véhicule assuré.
         */
        @NotNull(message = "L'ID du véhicule est obligatoire")
        UUID vehicleId,

        /**
         * ID du conducteur principal.
         */
        @NotNull(message = "L'ID du conducteur principal est obligatoire")
        UUID primaryDriverId,

        /**
         * Type de couverture.
         */
        @NotNull(message = "Le type de couverture est obligatoire")
        CoverageType coverageType,

        /**
         * Coefficient de bonus-malus.
         */
        @NotNull(message = "Le coefficient de bonus-malus est obligatoire")
        @DecimalMin(value = "0.5", inclusive = true, message = "Le coefficient de bonus-malus doit être supérieur ou égal à 0.5")
        @DecimalMax(value = "3.5", inclusive = true, message = "Le coefficient de bonus-malus doit être inférieur ou égal à 3.5")
        BigDecimal bonusMalusCoefficient,

        /**
         * Kilométrage annuel.
         */
        @Min(value = 0, message = "Le kilométrage annuel doit être supérieur ou égal à 0")
        @Max(value = 100000, message = "Le kilométrage annuel doit être inférieur ou égal à 100 000")
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
        @NotNull(message = "L'ID de la catégorie d'historique de sinistres est obligatoire")
        UUID claimHistoryCategoryId
) {
    /**
     * Builder pour CreateAutoPolicyRequest.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour CreateAutoPolicyRequest.
     */
    public static class Builder {
        private String policyNumber;
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal premiumAmount;
        private UUID vehicleId;
        private UUID primaryDriverId;
        private CoverageType coverageType;
        private BigDecimal bonusMalusCoefficient;
        private Integer annualMileage;
        private ParkingType parkingType;
        private boolean hasAntiTheftDevice;
        private UUID claimHistoryCategoryId;

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

        public Builder premiumAmount(BigDecimal premiumAmount) {
            this.premiumAmount = premiumAmount;
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

        public CreateAutoPolicyRequest build() {
            return new CreateAutoPolicyRequest(
                    policyNumber, startDate, endDate, premiumAmount, vehicleId, primaryDriverId,
                    coverageType, bonusMalusCoefficient, annualMileage, parkingType,
                    hasAntiTheftDevice, claimHistoryCategoryId
            );
        }
    }
}

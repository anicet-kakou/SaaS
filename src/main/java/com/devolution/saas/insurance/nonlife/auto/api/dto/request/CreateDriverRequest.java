package com.devolution.saas.insurance.nonlife.auto.api.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Requête pour la création d'un conducteur.
 */
public record CreateDriverRequest(
        /**
         * ID du client associé au conducteur.
         */
        @NotNull(message = "L'ID du client est obligatoire")
        UUID customerId,

        /**
         * Numéro de permis de conduire.
         */
        @NotBlank(message = "Le numéro de permis est obligatoire")
        @Size(min = 2, max = 50, message = "Le numéro de permis doit contenir entre 2 et 50 caractères")
        @Pattern(regexp = "^[A-Z0-9-]+$", message = "Le numéro de permis ne doit contenir que des lettres majuscules, des chiffres et des tirets")
        String licenseNumber,

        /**
         * ID du type de permis.
         */
        @NotNull(message = "L'ID du type de permis est obligatoire")
        UUID licenseTypeId,

        /**
         * Date de délivrance du permis.
         */
        @NotNull(message = "La date de délivrance du permis est obligatoire")
        @Past(message = "La date de délivrance du permis doit être dans le passé")
        LocalDate licenseIssueDate,

        /**
         * Date d'expiration du permis.
         */
        @Future(message = "La date d'expiration du permis doit être dans le futur")
        LocalDate licenseExpiryDate,

        /**
         * Indique si le conducteur est le conducteur principal.
         */
        boolean isPrimaryDriver,

        /**
         * Nombre d'années d'expérience de conduite.
         */
        @NotNull(message = "Le nombre d'années d'expérience est obligatoire")
        @Min(value = 0, message = "Le nombre d'années d'expérience doit être supérieur ou égal à 0")
        @Max(value = 100, message = "Le nombre d'années d'expérience doit être inférieur ou égal à 100")
        Integer yearsOfDrivingExperience
) {
    /**
     * Builder pour CreateDriverRequest.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour CreateDriverRequest.
     */
    public static class Builder {
        private UUID customerId;
        private String licenseNumber;
        private UUID licenseTypeId;
        private LocalDate licenseIssueDate;
        private LocalDate licenseExpiryDate;
        private boolean isPrimaryDriver;
        private Integer yearsOfDrivingExperience;

        public Builder customerId(UUID customerId) {
            this.customerId = customerId;
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

        public Builder yearsOfDrivingExperience(Integer yearsOfDrivingExperience) {
            this.yearsOfDrivingExperience = yearsOfDrivingExperience;
            return this;
        }

        public CreateDriverRequest build() {
            return new CreateDriverRequest(
                    customerId, licenseNumber, licenseTypeId, licenseIssueDate,
                    licenseExpiryDate, isPrimaryDriver, yearsOfDrivingExperience
            );
        }
    }
}

package com.devolution.saas.insurance.nonlife.auto.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Requête pour la création d'un conducteur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDriverRequest {

    /**
     * ID du client associé au conducteur.
     */
    @NotNull(message = "L'ID du client est obligatoire")
    private UUID customerId;

    /**
     * Numéro de permis de conduire.
     */
    @NotBlank(message = "Le numéro de permis est obligatoire")
    @Size(min = 2, max = 50, message = "Le numéro de permis doit contenir entre 2 et 50 caractères")
    private String licenseNumber;

    /**
     * ID du type de permis.
     */
    @NotNull(message = "L'ID du type de permis est obligatoire")
    private UUID licenseTypeId;

    /**
     * Date de délivrance du permis.
     */
    @NotNull(message = "La date de délivrance du permis est obligatoire")
    private LocalDate licenseIssueDate;

    /**
     * Date d'expiration du permis.
     */
    private LocalDate licenseExpiryDate;

    /**
     * Indique si le conducteur est le conducteur principal.
     */
    private boolean isPrimaryDriver;

    /**
     * Nombre d'années d'expérience de conduite.
     */
    @NotNull(message = "Le nombre d'années d'expérience est obligatoire")
    @Min(value = 0, message = "Le nombre d'années d'expérience doit être supérieur ou égal à 0")
    private Integer yearsOfDrivingExperience;
}

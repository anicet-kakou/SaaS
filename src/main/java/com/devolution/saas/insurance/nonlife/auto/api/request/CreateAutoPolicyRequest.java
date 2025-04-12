package com.devolution.saas.insurance.nonlife.auto.api.request;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.CoverageType;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.ParkingType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Requête pour la création d'une police d'assurance automobile.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAutoPolicyRequest {

    /**
     * Numéro de police.
     */
    @NotBlank(message = "Le numéro de police est obligatoire")
    @Size(min = 2, max = 50, message = "Le numéro de police doit contenir entre 2 et 50 caractères")
    private String policyNumber;

    /**
     * Date de début de la police.
     */
    @NotNull(message = "La date de début est obligatoire")
    private LocalDate startDate;

    /**
     * Date de fin de la police.
     */
    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate endDate;

    /**
     * Montant de la prime.
     */
    @NotNull(message = "Le montant de la prime est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant de la prime doit être supérieur à 0")
    private BigDecimal premiumAmount;

    /**
     * ID du véhicule assuré.
     */
    @NotNull(message = "L'ID du véhicule est obligatoire")
    private UUID vehicleId;

    /**
     * ID du conducteur principal.
     */
    @NotNull(message = "L'ID du conducteur principal est obligatoire")
    private UUID primaryDriverId;

    /**
     * Type de couverture.
     */
    @NotNull(message = "Le type de couverture est obligatoire")
    private CoverageType coverageType;

    /**
     * Coefficient de bonus-malus.
     */
    @NotNull(message = "Le coefficient de bonus-malus est obligatoire")
    @DecimalMin(value = "0.5", inclusive = true, message = "Le coefficient de bonus-malus doit être supérieur ou égal à 0.5")
    private BigDecimal bonusMalusCoefficient;

    /**
     * Kilométrage annuel.
     */
    @Min(value = 0, message = "Le kilométrage annuel doit être supérieur ou égal à 0")
    private Integer annualMileage;

    /**
     * Type de stationnement.
     */
    private ParkingType parkingType;

    /**
     * Indique si le véhicule est équipé d'un dispositif antivol.
     */
    private boolean hasAntiTheftDevice;

    /**
     * ID de la catégorie d'historique de sinistres.
     */
    @NotNull(message = "L'ID de la catégorie d'historique de sinistres est obligatoire")
    private UUID claimHistoryCategoryId;
}

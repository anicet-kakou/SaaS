package com.devolution.saas.insurance.nonlife.auto.application.command;

import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.CoverageType;
import com.devolution.saas.insurance.nonlife.auto.domain.model.AutoPolicy.ParkingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Commande pour la création d'une police d'assurance automobile.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAutoPolicyCommand {

    /**
     * Numéro de police.
     */
    private String policyNumber;

    /**
     * Date de début de la police.
     */
    private LocalDate startDate;

    /**
     * Date de fin de la police.
     */
    private LocalDate endDate;

    /**
     * ID du véhicule assuré.
     */
    private UUID vehicleId;

    /**
     * ID du conducteur principal.
     */
    private UUID primaryDriverId;

    /**
     * Type de couverture.
     */
    private CoverageType coverageType;

    /**
     * Coefficient de bonus-malus.
     */
    private BigDecimal bonusMalusCoefficient;

    /**
     * Kilométrage annuel.
     */
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
    private UUID claimHistoryCategoryId;

    /**
     * ID de l'organisation.
     */
    private UUID organizationId;
}

package com.devolution.saas.insurance.nonlife.auto.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entité représentant une police d'assurance automobile.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoPolicy {
    private UUID policyId;
    private UUID vehicleId;
    private UUID primaryDriverId;
    private CoverageType coverageType;
    private BigDecimal bonusMalusCoefficient;
    private Integer annualMileage;
    private ParkingType parkingType;
    private boolean hasAntiTheftDevice;
    private UUID claimHistoryCategoryId;
    private UUID organizationId;

    /**
     * Types de couverture pour une police auto.
     */
    public enum CoverageType {
        THIRD_PARTY,
        COMPREHENSIVE
    }

    /**
     * Types de stationnement pour un véhicule.
     */
    public enum ParkingType {
        GARAGE,
        STREET,
        PARKING_LOT
    }
}

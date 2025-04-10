package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entité représentant un type de carrosserie de véhicule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleBodyType {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private BigDecimal riskCoefficient;
    private boolean isActive;
    private UUID organizationId;
}

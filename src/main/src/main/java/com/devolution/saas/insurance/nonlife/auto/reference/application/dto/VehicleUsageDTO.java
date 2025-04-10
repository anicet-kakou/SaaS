package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO pour les types d'usage de véhicule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleUsageDTO {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private BigDecimal tariffCoefficient;
    private boolean isActive;
    private UUID organizationId;
}

package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO pour les types d'usage de véhicule.
 */
public record VehicleUsageDTO(
        UUID id,
        String code,
        String name,
        String description,
        BigDecimal tariffCoefficient,
        boolean isActive,
        UUID organizationId
) {
}

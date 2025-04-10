package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO pour les genres de véhicule.
 */
public record VehicleGenreDTO(
        UUID id,
        String code,
        String name,
        String description,
        BigDecimal riskCoefficient,
        boolean isActive,
        UUID organizationId
) {
}

package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO pour les types de carrosserie de véhicule.
 */
public record VehicleBodyTypeDTO(
        UUID id,
        String code,
        String name,
        String description,
        BigDecimal riskCoefficient,
        boolean isActive,
        UUID organizationId
) {
}

package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO pour les zones de circulation.
 */
public record CirculationZoneDTO(
        UUID id,
        String code,
        String name,
        String description,
        BigDecimal riskCoefficient,
        boolean isActive,
        UUID organizationId
) {
}

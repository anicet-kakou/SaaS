package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import java.util.UUID;

/**
 * DTO pour les types de carburant.
 */
public record FuelTypeDTO(
        UUID id,
        String code,
        String name,
        String description,
        boolean isActive,
        UUID organizationId
) {
}

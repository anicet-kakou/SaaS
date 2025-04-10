package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import java.util.UUID;

/**
 * DTO pour les couleurs de véhicule.
 */
public record VehicleColorDTO(
        UUID id,
        String code,
        String name,
        String description,
        boolean isActive,
        UUID organizationId
) {
}

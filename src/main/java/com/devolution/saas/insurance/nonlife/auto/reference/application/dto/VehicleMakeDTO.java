package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import java.util.UUID;

/**
 * DTO pour les marques de véhicule.
 */
public record VehicleMakeDTO(
        UUID id,
        String code,
        String name,
        String description,
        String countryOfOrigin,
        boolean isActive,
        UUID organizationId
) {
}

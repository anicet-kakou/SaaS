package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import java.util.UUID;

/**
 * DTO pour les modèles de véhicule.
 */
public record VehicleModelDTO(
        UUID id,
        UUID makeId,
        String makeName,
        String code,
        String name,
        String description,
        UUID categoryId,
        String categoryName,
        UUID subcategoryId,
        String subcategoryName,
        boolean isActive,
        UUID organizationId
) {
}

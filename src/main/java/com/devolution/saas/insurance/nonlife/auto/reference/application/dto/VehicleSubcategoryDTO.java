package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO pour les sous-catégories de véhicule.
 */
public record VehicleSubcategoryDTO(
        UUID id,
        UUID categoryId,
        String categoryName,
        String code,
        String name,
        String description,
        BigDecimal tariffCoefficient,
        boolean isActive,
        UUID organizationId
) {
}

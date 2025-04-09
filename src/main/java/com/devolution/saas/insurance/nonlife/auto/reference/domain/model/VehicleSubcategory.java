package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entité représentant une sous-catégorie de véhicule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSubcategory {
    private UUID id;
    private UUID categoryId;
    private String code;
    private String name;
    private String description;
    private BigDecimal tariffCoefficient;
    private boolean isActive;
    private UUID organizationId;
}

package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Entité représentant un modèle de véhicule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleModel {
    private UUID id;
    private UUID makeId;
    private String code;
    private String name;
    private String description;
    private UUID categoryId;
    private UUID subcategoryId;
    private boolean isActive;
    private UUID organizationId;
}

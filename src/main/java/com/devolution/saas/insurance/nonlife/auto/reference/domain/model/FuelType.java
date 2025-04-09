package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Entité représentant un type de carburant.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuelType {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private boolean isActive;
    private UUID organizationId;
}

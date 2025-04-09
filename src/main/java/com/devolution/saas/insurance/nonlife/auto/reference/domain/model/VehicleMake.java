package com.devolution.saas.insurance.nonlife.auto.reference.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Entité représentant une marque de véhicule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleMake {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private String countryOfOrigin;
    private boolean isActive;
    private UUID organizationId;
}

package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO pour les marques de véhicule.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleMakeDTO {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private String countryOfOrigin;
    private boolean isActive;
    private UUID organizationId;
}

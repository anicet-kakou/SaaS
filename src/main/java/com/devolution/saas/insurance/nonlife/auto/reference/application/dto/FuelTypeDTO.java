package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO pour les types de carburant.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuelTypeDTO {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private boolean isActive;
}

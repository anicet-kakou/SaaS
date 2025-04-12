package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * DTO pour les fabricants de véhicule.
 */
public record VehicleManufacturerDTO(
        UUID id,

        @NotBlank(message = "Le code du fabricant est obligatoire")
        @Size(min = 2, max = 20, message = "Le code doit contenir entre 2 et 20 caractères")
        @Pattern(regexp = "^[A-Z0-9_]+$", message = "Le code ne doit contenir que des lettres majuscules, des chiffres et des underscores")
        String code,

        @NotBlank(message = "Le nom du fabricant est obligatoire")
        @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
        String name,

        @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
        String description,

        @Size(max = 50, message = "Le pays d'origine ne doit pas dépasser 50 caractères")
        String countryOfOrigin,

        boolean isActive,

        @NotNull(message = "L'ID de l'organisation est obligatoire")
        UUID organizationId
) {
}

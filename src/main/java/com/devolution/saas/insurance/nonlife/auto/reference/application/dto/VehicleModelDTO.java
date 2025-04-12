package com.devolution.saas.insurance.nonlife.auto.reference.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * DTO pour les modèles de véhicule.
 */
public record VehicleModelDTO(
        UUID id,

        @NotNull(message = "L'ID du fabricant est obligatoire")
        UUID makeId,

        String makeName,

        @NotBlank(message = "Le code du modèle est obligatoire")
        @Size(min = 2, max = 20, message = "Le code doit contenir entre 2 et 20 caractères")
        @Pattern(regexp = "^[A-Z0-9_]+$", message = "Le code ne doit contenir que des lettres majuscules, des chiffres et des underscores")
        String code,

        @NotBlank(message = "Le nom du modèle est obligatoire")
        @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
        String name,

        @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
        String description,

        @NotNull(message = "L'ID de la catégorie est obligatoire")
        UUID categoryId,

        String categoryName,

        UUID subcategoryId,

        String subcategoryName,

        boolean isActive,

        @NotNull(message = "L'ID de l'organisation est obligatoire")
        UUID organizationId
) {
}

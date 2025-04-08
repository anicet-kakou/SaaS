package com.devolution.saas.core.security.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Commande pour la création d'une permission.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePermissionCommand {

    /**
     * Nom de la permission.
     * Si non spécifié, il sera généré à partir du type de ressource et de l'action.
     */
    private String name;

    /**
     * Description de la permission.
     */
    @Size(max = 255, message = "La description de la permission ne doit pas dépasser 255 caractères")
    private String description;

    /**
     * Type de ressource concernée par la permission.
     */
    @NotBlank(message = "Le type de ressource est obligatoire")
    @Size(min = 2, max = 50, message = "Le type de ressource doit contenir entre 2 et 50 caractères")
    private String resourceType;

    /**
     * Action autorisée sur la ressource.
     */
    @NotBlank(message = "L'action est obligatoire")
    @Size(min = 2, max = 50, message = "L'action doit contenir entre 2 et 50 caractères")
    private String action;

    /**
     * Indique si la permission est définie par le système.
     */
    @Builder.Default
    private boolean systemDefined = false;
}

package com.devolution.saas.core.security.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Commande pour la mise à jour d'une permission.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePermissionCommand {

    /**
     * ID de la permission à mettre à jour.
     */
    @NotNull(message = "L'ID de la permission est obligatoire")
    private UUID id;

    /**
     * Nom de la permission.
     */
    @NotBlank(message = "Le nom de la permission est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom de la permission doit contenir entre 2 et 100 caractères")
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
}

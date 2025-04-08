package com.devolution.saas.core.security.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Commande pour la création d'un rôle.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleCommand {

    /**
     * Nom du rôle.
     */
    @NotBlank(message = "Le nom du rôle est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom du rôle doit contenir entre 2 et 50 caractères")
    private String name;

    /**
     * Description du rôle.
     */
    @Size(max = 255, message = "La description du rôle ne doit pas dépasser 255 caractères")
    private String description;

    /**
     * ID de l'organisation.
     */
    @NotNull(message = "L'ID de l'organisation est obligatoire")
    private UUID organizationId;

    /**
     * IDs des permissions à associer au rôle.
     */
    @Builder.Default
    private Set<UUID> permissionIds = new HashSet<>();
}

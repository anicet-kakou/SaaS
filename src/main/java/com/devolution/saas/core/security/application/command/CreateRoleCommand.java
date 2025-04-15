package com.devolution.saas.core.security.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Commande pour la création d'un rôle.
 */
public record CreateRoleCommand(
    /**
     * Nom du rôle.
     */
    @NotBlank(message = "Le nom du rôle est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom du rôle doit contenir entre 2 et 50 caractères")
    String name,

    /**
     * Description du rôle.
     */
    @Size(max = 255, message = "La description du rôle ne doit pas dépasser 255 caractères")
    String description,

    /**
     * ID de l'organisation.
     */
    @NotNull(message = "L'ID de l'organisation est obligatoire")
    UUID organizationId,

    /**
     * IDs des permissions à associer au rôle.
     */
    Set<UUID> permissionIds
) {
    /**
     * Constructeur compact avec valeurs par défaut pour les collections.
     */
    public CreateRoleCommand {
        permissionIds = permissionIds != null ? permissionIds : new HashSet<>();
    }

    /**
     * Builder pour CreateRoleCommand.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour CreateRoleCommand.
     */
    public static class Builder {
        private String name;
        private String description;
        private UUID organizationId;
        private Set<UUID> permissionIds = new HashSet<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder permissionIds(Set<UUID> permissionIds) {
            this.permissionIds = permissionIds;
            return this;
        }

        public Builder addPermissionId(UUID permissionId) {
            if (this.permissionIds == null) {
                this.permissionIds = new HashSet<>();
            }
            this.permissionIds.add(permissionId);
            return this;
        }

        public CreateRoleCommand build() {
            return new CreateRoleCommand(name, description, organizationId, permissionIds);
        }
    }
}

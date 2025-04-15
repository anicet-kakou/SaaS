package com.devolution.saas.core.security.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Commande pour la création d'une permission.
 */
public record CreatePermissionCommand(
    /**
     * Nom de la permission.
     * Si non spécifié, il sera généré à partir du type de ressource et de l'action.
     */
    String name,

    /**
     * Description de la permission.
     */
    @Size(max = 255, message = "La description de la permission ne doit pas dépasser 255 caractères")
    String description,

    /**
     * Type de ressource concernée par la permission.
     */
    @NotBlank(message = "Le type de ressource est obligatoire")
    @Size(min = 2, max = 50, message = "Le type de ressource doit contenir entre 2 et 50 caractères")
    String resourceType,

    /**
     * Action autorisée sur la ressource.
     */
    @NotBlank(message = "L'action est obligatoire")
    @Size(min = 2, max = 50, message = "L'action doit contenir entre 2 et 50 caractères")
    String action,

    /**
     * Indique si la permission est définie par le système.
     */
    boolean systemDefined
) {
    /**
     * Constructeur avec valeur par défaut pour systemDefined.
     */
    public CreatePermissionCommand(String name, String description, String resourceType, String action) {
        this(name, description, resourceType, action, false);
    }

    /**
     * Builder pour CreatePermissionCommand.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour CreatePermissionCommand.
     */
    public static class Builder {
        private String name;
        private String description;
        private String resourceType;
        private String action;
        private boolean systemDefined = false;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder resourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder systemDefined(boolean systemDefined) {
            this.systemDefined = systemDefined;
            return this;
        }

        public CreatePermissionCommand build() {
            return new CreatePermissionCommand(name, description, resourceType, action, systemDefined);
        }
    }
}

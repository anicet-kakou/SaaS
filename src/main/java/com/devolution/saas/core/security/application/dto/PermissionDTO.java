package com.devolution.saas.core.security.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour les permissions.
 *
 * @param id            Identifiant unique de la permission
 * @param name          Nom de la permission
 * @param description   Description de la permission
 * @param resourceType  Type de ressource concernée par la permission
 * @param action        Action autorisée sur la ressource
 * @param createdAt     Date et heure de création
 * @param updatedAt     Date et heure de dernière mise à jour
 * @param systemDefined Indique si la permission est définie par le système
 * @author Cyr Leonce Anicet KAKOU <cyrkakou@gmail.com>
 */
public record PermissionDTO(
        UUID id,
        String name,
        String description,
        String resourceType,
        String action,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean systemDefined
) {
    /**
     * Crée un nouveau builder pour PermissionDTO.
     *
     * @return Builder pour PermissionDTO
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder pattern pour PermissionDTO.
     */
    public static class Builder {
        private UUID id;
        private String name;
        private String description;
        private String resourceType;
        private String action;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private boolean systemDefined;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

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

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder systemDefined(boolean systemDefined) {
            this.systemDefined = systemDefined;
            return this;
        }

        public PermissionDTO build() {
            return new PermissionDTO(id, name, description, resourceType,
                    action, createdAt, updatedAt, systemDefined);
        }
    }
}

package com.devolution.saas.core.security.application.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * DTO pour les rôles.
 *
 * @param id             Identifiant unique du rôle
 * @param name           Nom du rôle
 * @param description    Description du rôle
 * @param organizationId ID de l'organisation
 * @param createdAt      Date et heure de création
 * @param updatedAt      Date et heure de dernière mise à jour
 * @param systemDefined  Indique si le rôle est défini par le système
 * @param permissions    Permissions associées au rôle
 * @author Cyr Leonce Anicet KAKOU <cyrkakou@gmail.com>
 */
public record RoleDTO(
        UUID id,
        String name,
        String description,
        UUID organizationId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean systemDefined,
        Set<PermissionDTO> permissions
) {
    /**
     * Constructeur compact avec valeurs par défaut pour les collections.
     */
    public RoleDTO {
        permissions = permissions != null ? permissions : new HashSet<>();
    }

    /**
     * Crée un nouveau builder pour RoleDTO.
     *
     * @return Builder pour RoleDTO
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder pattern pour RoleDTO.
     */
    public static class Builder {
        private UUID id;
        private String name;
        private String description;
        private UUID organizationId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private boolean systemDefined;
        private Set<PermissionDTO> permissions = new HashSet<>();

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

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
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

        public Builder permissions(Set<PermissionDTO> permissions) {
            this.permissions = permissions != null ? permissions : new HashSet<>();
            return this;
        }

        public RoleDTO build() {
            return new RoleDTO(id, name, description, organizationId,
                    createdAt, updatedAt, systemDefined, permissions);
        }
    }
}

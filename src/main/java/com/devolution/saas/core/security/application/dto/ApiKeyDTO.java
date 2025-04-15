package com.devolution.saas.core.security.application.dto;

import com.devolution.saas.core.security.domain.model.ApiKeyStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * DTO pour les clés API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiKeyDTO(
    /**
     * ID de la clé API.
     */
    UUID id,

    /**
     * Nom de la clé API.
     */
    String name,

    /**
     * Préfixe de la clé API (partie visible).
     */
    String prefix,

    /**
     * Valeur complète de la clé API (uniquement lors de la création).
     */
    String key,

    /**
     * ID de l'organisation.
     */
    UUID organizationId,

    /**
     * Statut de la clé API.
     */
    ApiKeyStatus status,

    /**
     * Date d'expiration de la clé API.
     */
    LocalDateTime expiresAt,

    /**
     * Date de dernière utilisation de la clé API.
     */
    LocalDateTime lastUsedAt,

    /**
     * Permissions associées à la clé API.
     */
    String permissions,

    /**
     * Adresses IP autorisées à utiliser cette clé API.
     */
    Set<String> allowedIps,

    /**
     * Limite de taux de requêtes par minute.
     */
    Integer rateLimit,

    /**
     * Description de la clé API.
     */
    String description,

    /**
     * Date de création.
     */
    LocalDateTime createdAt,

    /**
     * Date de dernière mise à jour.
     */
    LocalDateTime updatedAt,

    /**
     * Indique si la clé API est active.
     */
    boolean active
) {
    /**
     * Constructeur par défaut avec des valeurs par défaut pour certains champs.
     */
    public ApiKeyDTO {
        if (allowedIps == null) {
            allowedIps = new HashSet<>();
        }
    }

    /**
     * Builder pour ApiKeyDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour ApiKeyDTO.
     */
    public static class Builder {
        private UUID id;
        private String name;
        private String prefix;
        private String key;
        private UUID organizationId;
        private ApiKeyStatus status;
        private LocalDateTime expiresAt;
        private LocalDateTime lastUsedAt;
        private String permissions;
        private Set<String> allowedIps = new HashSet<>();
        private Integer rateLimit;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private boolean active;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder status(ApiKeyStatus status) {
            this.status = status;
            return this;
        }

        public Builder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder lastUsedAt(LocalDateTime lastUsedAt) {
            this.lastUsedAt = lastUsedAt;
            return this;
        }

        public Builder permissions(String permissions) {
            this.permissions = permissions;
            return this;
        }

        public Builder allowedIps(Set<String> allowedIps) {
            this.allowedIps = allowedIps;
            return this;
        }

        public Builder rateLimit(Integer rateLimit) {
            this.rateLimit = rateLimit;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
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

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public ApiKeyDTO build() {
            return new ApiKeyDTO(
                    id, name, prefix, key, organizationId, status, expiresAt, lastUsedAt,
                    permissions, allowedIps, rateLimit, description, createdAt, updatedAt, active);
        }
    }
}

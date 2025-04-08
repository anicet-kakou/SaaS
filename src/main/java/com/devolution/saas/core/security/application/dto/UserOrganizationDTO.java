package com.devolution.saas.core.security.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour les relations utilisateur-organisation.
 *
 * @param id               Identifiant unique de la relation
 * @param userId           ID de l'utilisateur
 * @param organizationId   ID de l'organisation
 * @param organizationName Nom de l'organisation
 * @param organizationCode Code de l'organisation
 * @param createdAt        Date et heure de création
 * @param updatedAt        Date et heure de dernière mise à jour
 * @author Cyr Leonce Anicet KAKOU <cyrkakou@gmail.com>
 */
public record UserOrganizationDTO(
        UUID id,
        UUID userId,
        UUID organizationId,
        String organizationName,
        String organizationCode,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * Crée un nouveau builder pour UserOrganizationDTO.
     *
     * @return Builder pour UserOrganizationDTO
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder pattern pour UserOrganizationDTO.
     */
    public static class Builder {
        private UUID id;
        private UUID userId;
        private UUID organizationId;
        private String organizationName;
        private String organizationCode;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder organizationId(UUID organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder organizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public Builder organizationCode(String organizationCode) {
            this.organizationCode = organizationCode;
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

        public UserOrganizationDTO build() {
            return new UserOrganizationDTO(id, userId, organizationId,
                    organizationName, organizationCode, createdAt, updatedAt);
        }
    }
}

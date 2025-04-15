package com.devolution.saas.core.organization.application.dto;

import com.devolution.saas.core.organization.domain.model.OrganizationStatus;
import com.devolution.saas.core.organization.domain.model.OrganizationType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour les organisations.
 */
public record OrganizationDTO(
    /**
     * ID de l'organisation.
     */
    UUID id,

    /**
     * Nom de l'organisation.
     */
    String name,

    /**
     * Code de l'organisation.
     */
    String code,

    /**
     * Type de l'organisation.
     */
    OrganizationType type,

    /**
     * Statut de l'organisation.
     */
    OrganizationStatus status,

    /**
     * ID de l'organisation parente.
     */
    UUID parentId,

    /**
     * Nom de l'organisation parente.
     */
    String parentName,

    /**
     * Adresse de l'organisation.
     */
    String address,

    /**
     * Numéro de téléphone de l'organisation.
     */
    String phone,

    /**
     * Email de l'organisation.
     */
    String email,

    /**
     * Site web de l'organisation.
     */
    String website,

    /**
     * URL du logo de l'organisation.
     */
    String logoUrl,

    /**
     * Nom du contact principal de l'organisation.
     */
    String primaryContactName,

    /**
     * Description de l'organisation.
     */
    String description,

    /**
     * Paramètres de l'organisation au format JSON.
     */
    String settings,

    /**
     * Date de création de l'organisation.
     */
    LocalDateTime createdAt,

    /**
     * Date de dernière mise à jour de l'organisation.
     */
    LocalDateTime updatedAt,

    /**
     * Indique si l'organisation est active.
     */
    boolean active
) {
    /**
     * Builder pour OrganizationDTO.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour OrganizationDTO.
     */
    public static class Builder {
        private UUID id;
        private String name;
        private String code;
        private OrganizationType type;
        private OrganizationStatus status;
        private UUID parentId;
        private String parentName;
        private String address;
        private String phone;
        private String email;
        private String website;
        private String logoUrl;
        private String primaryContactName;
        private String description;
        private String settings;
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

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder type(OrganizationType type) {
            this.type = type;
            return this;
        }

        public Builder status(OrganizationStatus status) {
            this.status = status;
            return this;
        }

        public Builder parentId(UUID parentId) {
            this.parentId = parentId;
            return this;
        }

        public Builder parentName(String parentName) {
            this.parentName = parentName;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder website(String website) {
            this.website = website;
            return this;
        }

        public Builder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }

        public Builder primaryContactName(String primaryContactName) {
            this.primaryContactName = primaryContactName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder settings(String settings) {
            this.settings = settings;
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

        public OrganizationDTO build() {
            return new OrganizationDTO(
                    id, name, code, type, status, parentId, parentName,
                    address, phone, email, website, logoUrl, primaryContactName,
                    description, settings, createdAt, updatedAt, active);
        }
    }
}

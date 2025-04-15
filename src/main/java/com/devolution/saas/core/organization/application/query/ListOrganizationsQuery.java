package com.devolution.saas.core.organization.application.query;

import com.devolution.saas.core.organization.domain.model.OrganizationStatus;
import com.devolution.saas.core.organization.domain.model.OrganizationType;

import java.util.UUID;

/**
 * Requête pour lister les organisations.
 */
public record ListOrganizationsQuery(
    /**
     * Type d'organisation à filtrer.
     */
    OrganizationType type,

    /**
     * Statut d'organisation à filtrer.
     */
    OrganizationStatus status,

    /**
     * ID de l'organisation parente à filtrer.
     */
    UUID parentId,

    /**
     * Indique si seules les organisations racines doivent être retournées.
     */
    boolean rootsOnly,

    /**
     * Terme de recherche pour le nom ou le code.
     */
    String searchTerm,

    /**
     * Numéro de page pour la pagination.
     */
    int page,

    /**
     * Taille de page pour la pagination.
     */
    int size
) {
    /**
     * Constructeur par défaut avec valeurs par défaut pour la pagination.
     */
    public ListOrganizationsQuery {
        page = page < 0 ? 0 : page;
        size = size <= 0 ? 20 : size;
    }

    /**
     * Builder pour ListOrganizationsQuery.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Classe Builder pour ListOrganizationsQuery.
     */
    public static class Builder {
        private OrganizationType type;
        private OrganizationStatus status;
        private UUID parentId;
        private boolean rootsOnly;
        private String searchTerm;
        private int page = 0;
        private int size = 20;

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

        public Builder rootsOnly(boolean rootsOnly) {
            this.rootsOnly = rootsOnly;
            return this;
        }

        public Builder searchTerm(String searchTerm) {
            this.searchTerm = searchTerm;
            return this;
        }

        public Builder page(int page) {
            this.page = page;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public ListOrganizationsQuery build() {
            return new ListOrganizationsQuery(type, status, parentId, rootsOnly, searchTerm, page, size);
        }
    }
}

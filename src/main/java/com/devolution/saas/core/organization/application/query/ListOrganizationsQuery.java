package com.devolution.saas.core.organization.application.query;

import com.devolution.saas.core.organization.domain.model.OrganizationStatus;
import com.devolution.saas.core.organization.domain.model.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Requête pour lister les organisations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListOrganizationsQuery {

    /**
     * Type d'organisation à filtrer.
     */
    private OrganizationType type;

    /**
     * Statut d'organisation à filtrer.
     */
    private OrganizationStatus status;

    /**
     * ID de l'organisation parente à filtrer.
     */
    private UUID parentId;

    /**
     * Indique si seules les organisations racines doivent être retournées.
     */
    private boolean rootsOnly;

    /**
     * Terme de recherche pour le nom ou le code.
     */
    private String searchTerm;

    /**
     * Numéro de page pour la pagination.
     */
    @Builder.Default
    private int page = 0;

    /**
     * Taille de page pour la pagination.
     */
    @Builder.Default
    private int size = 20;
}

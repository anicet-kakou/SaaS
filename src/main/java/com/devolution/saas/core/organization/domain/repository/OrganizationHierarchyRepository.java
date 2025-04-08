package com.devolution.saas.core.organization.domain.repository;

import com.devolution.saas.core.organization.domain.model.OrganizationHierarchy;

import java.util.List;
import java.util.UUID;

/**
 * Interface de repository pour les opérations sur la hiérarchie des organisations.
 */
public interface OrganizationHierarchyRepository {

    /**
     * Enregistre une entrée de hiérarchie.
     *
     * @param hierarchy Hiérarchie à enregistrer
     * @return Hiérarchie enregistrée
     */
    OrganizationHierarchy save(OrganizationHierarchy hierarchy);

    // La méthode saveAll est héritée de JpaRepository, pas besoin de la déclarer ici

    /**
     * Trouve toutes les entrées de hiérarchie où l'organisation est un ancêtre.
     *
     * @param ancestorId ID de l'organisation ancêtre
     * @return Liste des hiérarchies
     */
    List<OrganizationHierarchy> findAllByAncestorId(UUID ancestorId);

    /**
     * Trouve toutes les entrées de hiérarchie où l'organisation est un descendant.
     *
     * @param descendantId ID de l'organisation descendante
     * @return Liste des hiérarchies
     */
    List<OrganizationHierarchy> findAllByDescendantId(UUID descendantId);

    /**
     * Trouve toutes les entrées de hiérarchie où l'organisation est un ancêtre direct (distance = 1).
     *
     * @param ancestorId ID de l'organisation ancêtre
     * @return Liste des hiérarchies
     */
    List<OrganizationHierarchy> findAllByAncestorIdAndDistance(UUID ancestorId, int distance);

    /**
     * Trouve tous les descendants d'une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des IDs des organisations descendantes
     */
    List<UUID> findAllDescendantIds(UUID organizationId);

    /**
     * Trouve tous les ancêtres d'une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des IDs des organisations ancêtres
     */
    List<UUID> findAllAncestorIds(UUID organizationId);

    /**
     * Supprime toutes les entrées de hiérarchie liées à une organisation.
     *
     * @param organizationId ID de l'organisation
     */
    void deleteAllByOrganizationId(UUID organizationId);

    /**
     * Vérifie si une organisation est un ancêtre d'une autre.
     *
     * @param ancestorId   ID de l'organisation potentiellement ancêtre
     * @param descendantId ID de l'organisation potentiellement descendante
     * @return true si l'organisation est un ancêtre, false sinon
     */
    boolean isAncestorOf(UUID ancestorId, UUID descendantId);
}

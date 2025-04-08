package com.devolution.saas.core.organization.application.service;

import com.devolution.saas.common.annotation.Auditable;
import com.devolution.saas.common.domain.exception.ResourceNotFoundException;
import com.devolution.saas.core.organization.domain.model.Organization;
import com.devolution.saas.core.organization.domain.repository.OrganizationHierarchyRepository;
import com.devolution.saas.core.organization.domain.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service pour la gestion de la hiérarchie des organisations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationHierarchyService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationHierarchyRepository hierarchyRepository;

    /**
     * Récupère tous les descendants d'une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des IDs des organisations descendantes
     */
    @Transactional(readOnly = true)
    @Auditable(action = "GET_ORGANIZATION_DESCENDANTS")
    public List<UUID> getAllDescendantIds(UUID organizationId) {
        log.debug("Récupération de tous les descendants de l'organisation: {}", organizationId);

        // Vérification de l'existence de l'organisation
        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException("Organization", organizationId);
        }

        return hierarchyRepository.findAllDescendantIds(organizationId);
    }

    /**
     * Récupère tous les ancêtres d'une organisation.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des IDs des organisations ancêtres
     */
    @Transactional(readOnly = true)
    @Auditable(action = "GET_ORGANIZATION_ANCESTORS")
    public List<UUID> getAllAncestorIds(UUID organizationId) {
        log.debug("Récupération de tous les ancêtres de l'organisation: {}", organizationId);

        // Vérification de l'existence de l'organisation
        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException("Organization", organizationId);
        }

        return hierarchyRepository.findAllAncestorIds(organizationId);
    }

    /**
     * Vérifie si une organisation est un ancêtre d'une autre.
     *
     * @param ancestorId   ID de l'organisation potentiellement ancêtre
     * @param descendantId ID de l'organisation potentiellement descendante
     * @return true si l'organisation est un ancêtre, false sinon
     */
    @Transactional(readOnly = true)
    @Auditable(action = "CHECK_ORGANIZATION_ANCESTOR")
    public boolean isAncestorOf(UUID ancestorId, UUID descendantId) {
        log.debug("Vérification si l'organisation {} est un ancêtre de l'organisation {}", ancestorId, descendantId);

        // Vérification de l'existence des organisations
        if (!organizationRepository.existsById(ancestorId) || !organizationRepository.existsById(descendantId)) {
            return false;
        }

        return hierarchyRepository.isAncestorOf(ancestorId, descendantId);
    }

    /**
     * Récupère tous les IDs d'organisations visibles pour une organisation.
     * Cela inclut l'organisation elle-même et tous ses descendants.
     *
     * @param organizationId ID de l'organisation
     * @return Ensemble des IDs des organisations visibles
     */
    @Transactional(readOnly = true)
    @Auditable(action = "GET_VISIBLE_ORGANIZATION_IDS")
    public Set<UUID> getVisibleOrganizationIds(UUID organizationId) {
        log.debug("Récupération des organisations visibles pour l'organisation: {}", organizationId);

        // Vérification de l'existence de l'organisation
        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException("Organization", organizationId);
        }

        Set<UUID> visibleOrganizationIds = new HashSet<>();

        // Ajouter l'organisation elle-même
        visibleOrganizationIds.add(organizationId);

        // Ajouter tous les descendants
        visibleOrganizationIds.addAll(hierarchyRepository.findAllDescendantIds(organizationId));

        return visibleOrganizationIds;
    }

    /**
     * Récupère le chemin hiérarchique complet d'une organisation.
     * Le chemin est une liste d'IDs d'organisations, du plus haut ancêtre à l'organisation elle-même.
     *
     * @param organizationId ID de l'organisation
     * @return Liste des IDs des organisations dans le chemin hiérarchique
     */
    @Transactional(readOnly = true)
    @Auditable(action = "GET_ORGANIZATION_PATH")
    public List<UUID> getOrganizationPath(UUID organizationId) {
        log.debug("Récupération du chemin hiérarchique de l'organisation: {}", organizationId);

        // Vérification de l'existence de l'organisation
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", organizationId));

        List<UUID> path = new ArrayList<>();

        // Ajouter tous les ancêtres dans l'ordre inverse (du plus haut au plus bas)
        List<UUID> ancestors = hierarchyRepository.findAllAncestorIds(organizationId);
        if (!ancestors.isEmpty()) {
            // Trier les ancêtres par distance (du plus haut au plus bas)
            // Note: Ceci est une simplification, une implémentation plus complète
            // nécessiterait de récupérer les distances depuis la table de hiérarchie
            Organization current = organization;
            while (current.getParent() != null) {
                path.add(0, current.getParent().getId());
                current = current.getParent();
            }
        }

        // Ajouter l'organisation elle-même
        path.add(organizationId);

        return path;
    }
}

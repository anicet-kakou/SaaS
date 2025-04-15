package com.devolution.saas.insurance.nonlife.auto.reference.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.CirculationZone;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des zones de circulation.
 */
public interface CirculationZoneRepository {

    /**
     * Sauvegarde une zone de circulation.
     *
     * @param circulationZone La zone de circulation à sauvegarder
     * @return La zone de circulation sauvegardée
     */
    CirculationZone save(CirculationZone circulationZone);

    /**
     * Trouve une zone de circulation par son ID.
     *
     * @param id L'ID de la zone de circulation
     * @return La zone de circulation trouvée, ou empty si non trouvée
     */
    Optional<CirculationZone> findById(UUID id);

    /**
     * Trouve une zone de circulation par son code.
     *
     * @param code           Le code de la zone de circulation
     * @param organizationId L'ID de l'organisation
     * @return La zone de circulation trouvée, ou empty si non trouvée
     */
    Optional<CirculationZone> findByCodeAndOrganizationId(String code, UUID organizationId);

    /**
     * Liste toutes les zones de circulation d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones de circulation
     */
    List<CirculationZone> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste toutes les zones de circulation actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones de circulation actives
     */
    List<CirculationZone> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Liste toutes les zones de circulation par statut d'activité et organisation.
     *
     * @param isActive       Statut d'activité
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones de circulation correspondant aux critères
     */
    List<CirculationZone> findAllByIsActiveAndOrganizationId(boolean isActive, UUID organizationId);

    /**
     * Supprime une zone de circulation.
     *
     * @param id L'ID de la zone de circulation à supprimer
     */
    void deleteById(UUID id);
}

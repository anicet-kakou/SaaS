package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.CirculationZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.CirculationZone;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des zones de circulation.
 */
public interface CirculationZoneService {

    /**
     * Crée une nouvelle zone de circulation.
     *
     * @param circulationZone La zone de circulation à créer
     * @param organizationId  L'ID de l'organisation
     * @return La zone de circulation créée
     */
    CirculationZoneDTO createCirculationZone(CirculationZone circulationZone, UUID organizationId);

    /**
     * Met à jour une zone de circulation.
     *
     * @param id              L'ID de la zone de circulation
     * @param circulationZone La zone de circulation mise à jour
     * @param organizationId  L'ID de l'organisation
     * @return La zone de circulation mise à jour, ou empty si non trouvée
     */
    Optional<CirculationZoneDTO> updateCirculationZone(UUID id, CirculationZone circulationZone, UUID organizationId);

    /**
     * Récupère une zone de circulation par son ID.
     *
     * @param id             L'ID de la zone de circulation
     * @param organizationId L'ID de l'organisation
     * @return La zone de circulation trouvée, ou empty si non trouvée
     */
    Optional<CirculationZoneDTO> getCirculationZoneById(UUID id, UUID organizationId);

    /**
     * Récupère une zone de circulation par son code.
     *
     * @param code           Le code de la zone de circulation
     * @param organizationId L'ID de l'organisation
     * @return La zone de circulation trouvée, ou empty si non trouvée
     */
    Optional<CirculationZoneDTO> getCirculationZoneByCode(String code, UUID organizationId);

    /**
     * Liste toutes les zones de circulation d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones de circulation
     */
    List<CirculationZoneDTO> getAllCirculationZones(UUID organizationId);

    /**
     * Liste toutes les zones de circulation actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones de circulation actives
     */
    List<CirculationZoneDTO> getAllActiveCirculationZones(UUID organizationId);

    /**
     * Supprime une zone de circulation.
     *
     * @param id             L'ID de la zone de circulation
     * @param organizationId L'ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteCirculationZone(UUID id, UUID organizationId);
}

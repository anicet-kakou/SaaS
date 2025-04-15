package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.common.abstracts.TenantAwareCrudService;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.GeographicZoneDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.GeographicZone;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des zones géographiques.
 */
public interface GeographicZoneService extends TenantAwareCrudService<GeographicZoneDTO, GeographicZone> {

    /**
     * Crée une nouvelle zone géographique.
     *
     * @param geographicZone La zone géographique à créer
     * @param organizationId L'ID de l'organisation
     * @return La zone géographique créée
     */
    GeographicZoneDTO createGeographicZone(GeographicZone geographicZone, UUID organizationId);

    /**
     * Met à jour une zone géographique.
     *
     * @param id             L'ID de la zone géographique
     * @param geographicZone La zone géographique mise à jour
     * @param organizationId L'ID de l'organisation
     * @return La zone géographique mise à jour, ou empty si non trouvée
     */
    Optional<GeographicZoneDTO> updateGeographicZone(UUID id, GeographicZone geographicZone, UUID organizationId);

    /**
     * Récupère une zone géographique par son ID.
     *
     * @param id             L'ID de la zone géographique
     * @param organizationId L'ID de l'organisation
     * @return La zone géographique trouvée, ou empty si non trouvée
     */
    Optional<GeographicZoneDTO> getGeographicZoneById(UUID id, UUID organizationId);



    /**
     * Liste toutes les zones géographiques d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones géographiques
     */
    List<GeographicZoneDTO> getAllGeographicZones(UUID organizationId);

    /**
     * Liste toutes les zones géographiques actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones géographiques actives
     */
    List<GeographicZoneDTO> getAllActiveGeographicZones(UUID organizationId);

    /**
     * Supprime une zone géographique.
     *
     * @param id             L'ID de la zone géographique
     * @param organizationId L'ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteGeographicZone(UUID id, UUID organizationId);

    /**
     * Active ou désactive une zone géographique.
     *
     * @param id             L'ID de la zone géographique
     * @param active         Le statut d'activation
     * @param organizationId L'ID de l'organisation
     * @return La zone géographique mise à jour, ou empty si non trouvée
     */
    Optional<GeographicZoneDTO> setActive(UUID id, boolean active, UUID organizationId);
}

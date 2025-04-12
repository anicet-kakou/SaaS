package com.devolution.saas.insurance.nonlife.auto.reference.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.GeographicZone;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des zones géographiques.
 */
public interface GeographicZoneRepository {

    /**
     * Sauvegarde une zone géographique.
     *
     * @param geographicZone La zone géographique à sauvegarder
     * @return La zone géographique sauvegardée
     */
    GeographicZone save(GeographicZone geographicZone);

    /**
     * Trouve une zone géographique par son ID.
     *
     * @param id L'ID de la zone géographique
     * @return La zone géographique trouvée, ou empty si non trouvée
     */
    Optional<GeographicZone> findById(UUID id);

    /**
     * Trouve une zone géographique par son code.
     *
     * @param code           Le code de la zone géographique
     * @param organizationId L'ID de l'organisation
     * @return La zone géographique trouvée, ou empty si non trouvée
     */
    Optional<GeographicZone> findByCodeAndOrganizationId(String code, UUID organizationId);

    /**
     * Liste toutes les zones géographiques d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones géographiques
     */
    List<GeographicZone> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste toutes les zones géographiques actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des zones géographiques actives
     */
    List<GeographicZone> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Supprime une zone géographique.
     *
     * @param id L'ID de la zone géographique à supprimer
     */
    void deleteById(UUID id);
}

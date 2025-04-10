package com.devolution.saas.insurance.nonlife.auto.reference.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleGenre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des genres de véhicule.
 */
public interface VehicleGenreRepository {

    /**
     * Sauvegarde un genre de véhicule.
     *
     * @param vehicleGenre Le genre de véhicule à sauvegarder
     * @return Le genre de véhicule sauvegardé
     */
    VehicleGenre save(VehicleGenre vehicleGenre);

    /**
     * Trouve un genre de véhicule par son ID.
     *
     * @param id L'ID du genre de véhicule
     * @return Le genre de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleGenre> findById(UUID id);

    /**
     * Trouve un genre de véhicule par son code.
     *
     * @param code           Le code du genre de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le genre de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleGenre> findByCodeAndOrganizationId(String code, UUID organizationId);

    /**
     * Liste tous les genres de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des genres de véhicule
     */
    List<VehicleGenre> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste tous les genres de véhicule actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des genres de véhicule actifs
     */
    List<VehicleGenre> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Supprime un genre de véhicule.
     *
     * @param id L'ID du genre de véhicule à supprimer
     */
    void deleteById(UUID id);
}

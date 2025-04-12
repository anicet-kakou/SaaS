package com.devolution.saas.insurance.nonlife.auto.reference.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleManufacturer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des fabricants de véhicule.
 */
public interface VehicleManufacturerRepository {

    /**
     * Sauvegarde un fabricant de véhicule.
     *
     * @param vehicleManufacturer Le fabricant de véhicule à sauvegarder
     * @return Le fabricant de véhicule sauvegardé
     */
    VehicleManufacturer save(VehicleManufacturer vehicleManufacturer);

    /**
     * Trouve un fabricant de véhicule par son ID.
     *
     * @param id L'ID du fabricant de véhicule
     * @return Le fabricant de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleManufacturer> findById(UUID id);

    /**
     * Trouve un fabricant de véhicule par son code.
     *
     * @param code           Le code du fabricant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le fabricant de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleManufacturer> findByCodeAndOrganizationId(String code, UUID organizationId);

    /**
     * Liste tous les fabricants de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des fabricants de véhicule
     */
    List<VehicleManufacturer> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste tous les fabricants de véhicule actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des fabricants de véhicule actifs
     */
    List<VehicleManufacturer> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Supprime un fabricant de véhicule.
     *
     * @param id L'ID du fabricant de véhicule à supprimer
     */
    void deleteById(UUID id);
}

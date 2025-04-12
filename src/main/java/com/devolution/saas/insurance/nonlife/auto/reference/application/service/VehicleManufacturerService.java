package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleManufacturerDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleManufacturer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des fabricants de véhicule.
 */
public interface VehicleManufacturerService {

    /**
     * Crée un nouveau fabricant de véhicule.
     *
     * @param vehicleManufacturer Le fabricant de véhicule à créer
     * @param organizationId      L'ID de l'organisation
     * @return Le fabricant de véhicule créé
     */
    VehicleManufacturerDTO createVehicleManufacturer(VehicleManufacturer vehicleManufacturer, UUID organizationId);

    /**
     * Met à jour un fabricant de véhicule.
     *
     * @param id                  L'ID du fabricant de véhicule
     * @param vehicleManufacturer Le fabricant de véhicule mis à jour
     * @param organizationId      L'ID de l'organisation
     * @return Le fabricant de véhicule mis à jour, ou empty si non trouvé
     */
    Optional<VehicleManufacturerDTO> updateVehicleManufacturer(UUID id, VehicleManufacturer vehicleManufacturer, UUID organizationId);

    /**
     * Récupère un fabricant de véhicule par son ID.
     *
     * @param id             L'ID du fabricant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le fabricant de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleManufacturerDTO> getVehicleManufacturerById(UUID id, UUID organizationId);

    /**
     * Récupère un fabricant de véhicule par son code.
     *
     * @param code           Le code du fabricant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le fabricant de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleManufacturerDTO> getVehicleManufacturerByCode(String code, UUID organizationId);

    /**
     * Liste tous les fabricants de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des fabricants de véhicule
     */
    List<VehicleManufacturerDTO> getAllVehicleManufacturers(UUID organizationId);

    /**
     * Liste tous les fabricants de véhicule actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des fabricants de véhicule actifs
     */
    List<VehicleManufacturerDTO> getAllActiveVehicleManufacturers(UUID organizationId);

    /**
     * Supprime un fabricant de véhicule.
     *
     * @param id             L'ID du fabricant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteVehicleManufacturer(UUID id, UUID organizationId);
}

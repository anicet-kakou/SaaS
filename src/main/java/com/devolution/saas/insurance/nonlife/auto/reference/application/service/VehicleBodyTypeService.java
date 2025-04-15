package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.common.abstracts.TenantAwareCrudService;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleBodyTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleBodyType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des types de carrosserie de véhicule.
 */
public interface VehicleBodyTypeService extends TenantAwareCrudService<VehicleBodyTypeDTO, VehicleBodyType> {

    /**
     * Crée un nouveau type de carrosserie de véhicule.
     *
     * @param vehicleBodyType Le type de carrosserie de véhicule à créer
     * @param organizationId  L'ID de l'organisation
     * @return Le type de carrosserie de véhicule créé
     */
    VehicleBodyTypeDTO createVehicleBodyType(VehicleBodyType vehicleBodyType, UUID organizationId);

    /**
     * Met à jour un type de carrosserie de véhicule.
     *
     * @param id              L'ID du type de carrosserie de véhicule
     * @param vehicleBodyType Le type de carrosserie de véhicule mis à jour
     * @param organizationId  L'ID de l'organisation
     * @return Le type de carrosserie de véhicule mis à jour, ou empty si non trouvé
     */
    Optional<VehicleBodyTypeDTO> updateVehicleBodyType(UUID id, VehicleBodyType vehicleBodyType, UUID organizationId);

    /**
     * Récupère un type de carrosserie de véhicule par son ID.
     *
     * @param id             L'ID du type de carrosserie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type de carrosserie de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleBodyTypeDTO> getVehicleBodyTypeById(UUID id, UUID organizationId);

    /**
     * Récupère un type de carrosserie de véhicule par son code.
     *
     * @param code           Le code du type de carrosserie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type de carrosserie de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleBodyTypeDTO> getVehicleBodyTypeByCode(String code, UUID organizationId);

    /**
     * Liste tous les types de carrosserie de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carrosserie de véhicule
     */
    List<VehicleBodyTypeDTO> getAllVehicleBodyTypes(UUID organizationId);

    /**
     * Liste tous les types de carrosserie de véhicule actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carrosserie de véhicule actifs
     */
    List<VehicleBodyTypeDTO> getAllActiveVehicleBodyTypes(UUID organizationId);

    /**
     * Supprime un type de carrosserie de véhicule.
     *
     * @param id             L'ID du type de carrosserie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteVehicleBodyType(UUID id, UUID organizationId);
}

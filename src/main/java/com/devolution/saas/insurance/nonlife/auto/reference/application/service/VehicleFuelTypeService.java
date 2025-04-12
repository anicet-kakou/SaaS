package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleFuelTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleFuelType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des types de carburant de véhicule.
 */
public interface VehicleFuelTypeService {

    /**
     * Crée un nouveau type de carburant de véhicule.
     *
     * @param vehicleFuelType Le type de carburant de véhicule à créer
     * @param organizationId  L'ID de l'organisation
     * @return Le type de carburant de véhicule créé
     */
    VehicleFuelTypeDTO createVehicleFuelType(VehicleFuelType vehicleFuelType, UUID organizationId);

    /**
     * Met à jour un type de carburant de véhicule.
     *
     * @param id              L'ID du type de carburant de véhicule
     * @param vehicleFuelType Le type de carburant de véhicule mis à jour
     * @param organizationId  L'ID de l'organisation
     * @return Le type de carburant de véhicule mis à jour, ou empty si non trouvé
     */
    Optional<VehicleFuelTypeDTO> updateVehicleFuelType(UUID id, VehicleFuelType vehicleFuelType, UUID organizationId);

    /**
     * Récupère un type de carburant de véhicule par son ID.
     *
     * @param id             L'ID du type de carburant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleFuelTypeDTO> getVehicleFuelTypeById(UUID id, UUID organizationId);

    /**
     * Récupère un type de carburant de véhicule par son code.
     *
     * @param code           Le code du type de carburant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleFuelTypeDTO> getVehicleFuelTypeByCode(String code, UUID organizationId);

    /**
     * Liste tous les types de carburant de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant de véhicule
     */
    List<VehicleFuelTypeDTO> getAllVehicleFuelTypes(UUID organizationId);

    /**
     * Liste tous les types de carburant de véhicule actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant de véhicule actifs
     */
    List<VehicleFuelTypeDTO> getAllActiveVehicleFuelTypes(UUID organizationId);

    /**
     * Supprime un type de carburant de véhicule.
     *
     * @param id             L'ID du type de carburant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteVehicleFuelType(UUID id, UUID organizationId);
}

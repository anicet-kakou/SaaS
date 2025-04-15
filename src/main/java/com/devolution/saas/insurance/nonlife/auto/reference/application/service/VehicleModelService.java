package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.common.abstracts.TenantAwareCrudService;
import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleModelDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des modèles de véhicule.
 */
public interface VehicleModelService extends TenantAwareCrudService<VehicleModelDTO, VehicleModel> {

    /**
     * Crée un nouveau modèle de véhicule.
     *
     * @param vehicleModel   Le modèle de véhicule à créer
     * @param organizationId L'ID de l'organisation
     * @return Le modèle de véhicule créé
     */
    VehicleModelDTO createVehicleModel(VehicleModel vehicleModel, UUID organizationId);

    /**
     * Met à jour un modèle de véhicule.
     *
     * @param id             L'ID du modèle de véhicule
     * @param vehicleModel   Le modèle de véhicule mis à jour
     * @param organizationId L'ID de l'organisation
     * @return Le modèle de véhicule mis à jour, ou empty si non trouvé
     */
    Optional<VehicleModelDTO> updateVehicleModel(UUID id, VehicleModel vehicleModel, UUID organizationId);

    /**
     * Récupère un modèle de véhicule par son ID.
     *
     * @param id             L'ID du modèle de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le modèle de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleModelDTO> getVehicleModelById(UUID id, UUID organizationId);

    /**
     * Récupère un modèle de véhicule par son code et sa marque.
     *
     * @param code           Le code du modèle de véhicule
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return Le modèle de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleModelDTO> getVehicleModelByCodeAndMake(String code, UUID makeId, UUID organizationId);

    /**
     * Récupère un modèle de véhicule par son code.
     *
     * @param code           Le code du modèle de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le modèle de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleModelDTO> getVehicleModelByCode(String code, UUID organizationId);

    /**
     * Liste tous les modèles de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule
     */
    List<VehicleModelDTO> getAllVehicleModels(UUID organizationId);

    /**
     * Liste tous les modèles de véhicule actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule actifs
     */
    List<VehicleModelDTO> getAllActiveVehicleModels(UUID organizationId);

    /**
     * Liste tous les modèles de véhicule pour une marque donnée.
     *
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule pour la marque
     */
    List<VehicleModelDTO> getAllVehicleModelsByMake(UUID makeId, UUID organizationId);

    /**
     * Liste tous les modèles de véhicule actifs pour une marque donnée.
     *
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule actifs pour la marque
     */
    List<VehicleModelDTO> getAllActiveVehicleModelsByMake(UUID makeId, UUID organizationId);

    /**
     * Supprime un modèle de véhicule.
     *
     * @param id             L'ID du modèle de véhicule
     * @param organizationId L'ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteVehicleModel(UUID id, UUID organizationId);
}

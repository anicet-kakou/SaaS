package com.devolution.saas.insurance.nonlife.auto.reference.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des modèles de véhicule.
 */
public interface VehicleModelRepository {

    /**
     * Sauvegarde un modèle de véhicule.
     *
     * @param vehicleModel Le modèle de véhicule à sauvegarder
     * @return Le modèle de véhicule sauvegardé
     */
    VehicleModel save(VehicleModel vehicleModel);

    /**
     * Trouve un modèle de véhicule par son ID.
     *
     * @param id L'ID du modèle de véhicule
     * @return Le modèle de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleModel> findById(UUID id);

    /**
     * Trouve un modèle de véhicule par son code.
     *
     * @param code           Le code du modèle de véhicule
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return Le modèle de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleModel> findByCodeAndMakeIdAndOrganizationId(String code, UUID makeId, UUID organizationId);

    /**
     * Liste tous les modèles de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule
     */
    List<VehicleModel> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste tous les modèles de véhicule actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule actifs
     */
    List<VehicleModel> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Liste tous les modèles de véhicule pour une marque donnée.
     *
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule pour la marque
     */
    List<VehicleModel> findAllByMakeIdAndOrganizationId(UUID makeId, UUID organizationId);

    /**
     * Liste tous les modèles de véhicule actifs pour une marque donnée.
     *
     * @param makeId         L'ID de la marque
     * @param organizationId L'ID de l'organisation
     * @return La liste des modèles de véhicule actifs pour la marque
     */
    List<VehicleModel> findAllActiveByMakeIdAndOrganizationId(UUID makeId, UUID organizationId);

    /**
     * Supprime un modèle de véhicule.
     *
     * @param id L'ID du modèle de véhicule à supprimer
     */
    void deleteById(UUID id);
}

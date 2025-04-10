package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleUsageDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleUsage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des types d'usage de véhicule.
 */
public interface VehicleUsageService {

    /**
     * Crée un nouveau type d'usage de véhicule.
     *
     * @param vehicleUsage   Le type d'usage de véhicule à créer
     * @param organizationId L'ID de l'organisation
     * @return Le type d'usage de véhicule créé
     */
    VehicleUsageDTO createVehicleUsage(VehicleUsage vehicleUsage, UUID organizationId);

    /**
     * Met à jour un type d'usage de véhicule.
     *
     * @param id             L'ID du type d'usage de véhicule
     * @param vehicleUsage   Le type d'usage de véhicule mis à jour
     * @param organizationId L'ID de l'organisation
     * @return Le type d'usage de véhicule mis à jour, ou empty si non trouvé
     */
    Optional<VehicleUsageDTO> updateVehicleUsage(UUID id, VehicleUsage vehicleUsage, UUID organizationId);

    /**
     * Récupère un type d'usage de véhicule par son ID.
     *
     * @param id             L'ID du type d'usage de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type d'usage de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleUsageDTO> getVehicleUsageById(UUID id, UUID organizationId);

    /**
     * Récupère un type d'usage de véhicule par son code.
     *
     * @param code           Le code du type d'usage de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type d'usage de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleUsageDTO> getVehicleUsageByCode(String code, UUID organizationId);

    /**
     * Liste tous les types d'usage de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types d'usage de véhicule
     */
    List<VehicleUsageDTO> getAllVehicleUsages(UUID organizationId);

    /**
     * Liste tous les types d'usage de véhicule actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types d'usage de véhicule actifs
     */
    List<VehicleUsageDTO> getAllActiveVehicleUsages(UUID organizationId);

    /**
     * Supprime un type d'usage de véhicule.
     *
     * @param id             L'ID du type d'usage de véhicule
     * @param organizationId L'ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteVehicleUsage(UUID id, UUID organizationId);
}

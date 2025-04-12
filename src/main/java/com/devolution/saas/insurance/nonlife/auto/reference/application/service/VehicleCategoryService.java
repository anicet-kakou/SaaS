package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleCategoryDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des catégories de véhicule.
 */
public interface VehicleCategoryService {

    /**
     * Crée une nouvelle catégorie de véhicule.
     *
     * @param vehicleCategory La catégorie de véhicule à créer
     * @param organizationId  L'ID de l'organisation
     * @return La catégorie de véhicule créée
     */
    VehicleCategoryDTO createVehicleCategory(VehicleCategory vehicleCategory, UUID organizationId);

    /**
     * Met à jour une catégorie de véhicule.
     *
     * @param id              L'ID de la catégorie de véhicule
     * @param vehicleCategory La catégorie de véhicule mise à jour
     * @param organizationId  L'ID de l'organisation
     * @return La catégorie de véhicule mise à jour, ou empty si non trouvée
     */
    Optional<VehicleCategoryDTO> updateVehicleCategory(UUID id, VehicleCategory vehicleCategory, UUID organizationId);

    /**
     * Récupère une catégorie de véhicule par son ID.
     *
     * @param id             L'ID de la catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La catégorie de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleCategoryDTO> getVehicleCategoryById(UUID id, UUID organizationId);

    /**
     * Récupère une catégorie de véhicule par son code.
     *
     * @param code           Le code de la catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La catégorie de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleCategoryDTO> getVehicleCategoryByCode(String code, UUID organizationId);

    /**
     * Liste toutes les catégories de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des catégories de véhicule
     */
    List<VehicleCategoryDTO> getAllVehicleCategories(UUID organizationId);

    /**
     * Liste toutes les catégories de véhicule actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des catégories de véhicule actives
     */
    List<VehicleCategoryDTO> getAllActiveVehicleCategories(UUID organizationId);

    /**
     * Supprime une catégorie de véhicule.
     *
     * @param id             L'ID de la catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteVehicleCategory(UUID id, UUID organizationId);
}

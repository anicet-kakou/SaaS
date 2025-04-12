package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.VehicleSubcategoryDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleSubcategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des sous-catégories de véhicule.
 */
public interface VehicleSubcategoryService {

    /**
     * Crée une nouvelle sous-catégorie de véhicule.
     *
     * @param vehicleSubcategory La sous-catégorie de véhicule à créer
     * @param organizationId     L'ID de l'organisation
     * @return La sous-catégorie de véhicule créée
     */
    VehicleSubcategoryDTO createVehicleSubcategory(VehicleSubcategory vehicleSubcategory, UUID organizationId);

    /**
     * Met à jour une sous-catégorie de véhicule.
     *
     * @param id                 L'ID de la sous-catégorie de véhicule
     * @param vehicleSubcategory La sous-catégorie de véhicule mise à jour
     * @param organizationId     L'ID de l'organisation
     * @return La sous-catégorie de véhicule mise à jour, ou empty si non trouvée
     */
    Optional<VehicleSubcategoryDTO> updateVehicleSubcategory(UUID id, VehicleSubcategory vehicleSubcategory, UUID organizationId);

    /**
     * Récupère une sous-catégorie de véhicule par son ID.
     *
     * @param id             L'ID de la sous-catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La sous-catégorie de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleSubcategoryDTO> getVehicleSubcategoryById(UUID id, UUID organizationId);

    /**
     * Récupère une sous-catégorie de véhicule par son code.
     *
     * @param code           Le code de la sous-catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La sous-catégorie de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleSubcategoryDTO> getVehicleSubcategoryByCode(String code, UUID organizationId);

    /**
     * Liste toutes les sous-catégories de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule
     */
    List<VehicleSubcategoryDTO> getAllVehicleSubcategories(UUID organizationId);

    /**
     * Liste toutes les sous-catégories de véhicule actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule actives
     */
    List<VehicleSubcategoryDTO> getAllActiveVehicleSubcategories(UUID organizationId);

    /**
     * Liste toutes les sous-catégories de véhicule pour une catégorie donnée.
     *
     * @param categoryId     L'ID de la catégorie
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule pour la catégorie
     */
    List<VehicleSubcategoryDTO> getAllVehicleSubcategoriesByCategory(UUID categoryId, UUID organizationId);

    /**
     * Liste toutes les sous-catégories de véhicule actives pour une catégorie donnée.
     *
     * @param categoryId     L'ID de la catégorie
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule actives pour la catégorie
     */
    List<VehicleSubcategoryDTO> getAllActiveVehicleSubcategoriesByCategory(UUID categoryId, UUID organizationId);

    /**
     * Supprime une sous-catégorie de véhicule.
     *
     * @param id             L'ID de la sous-catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteVehicleSubcategory(UUID id, UUID organizationId);
}

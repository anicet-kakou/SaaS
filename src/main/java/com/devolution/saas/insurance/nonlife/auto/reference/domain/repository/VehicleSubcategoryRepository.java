package com.devolution.saas.insurance.nonlife.auto.reference.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleSubcategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des sous-catégories de véhicule.
 */
public interface VehicleSubcategoryRepository {

    /**
     * Sauvegarde une sous-catégorie de véhicule.
     *
     * @param vehicleSubcategory La sous-catégorie de véhicule à sauvegarder
     * @return La sous-catégorie de véhicule sauvegardée
     */
    VehicleSubcategory save(VehicleSubcategory vehicleSubcategory);

    /**
     * Trouve une sous-catégorie de véhicule par son ID.
     *
     * @param id L'ID de la sous-catégorie de véhicule
     * @return La sous-catégorie de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleSubcategory> findById(UUID id);

    /**
     * Trouve une sous-catégorie de véhicule par son code.
     *
     * @param code           Le code de la sous-catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La sous-catégorie de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleSubcategory> findByCodeAndOrganizationId(String code, UUID organizationId);

    /**
     * Liste toutes les sous-catégories de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule
     */
    List<VehicleSubcategory> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste toutes les sous-catégories de véhicule actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule actives
     */
    List<VehicleSubcategory> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Liste toutes les sous-catégories de véhicule pour une catégorie donnée.
     *
     * @param categoryId     L'ID de la catégorie
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule pour la catégorie
     */
    List<VehicleSubcategory> findAllByCategoryIdAndOrganizationId(UUID categoryId, UUID organizationId);

    /**
     * Liste toutes les sous-catégories de véhicule actives pour une catégorie donnée.
     *
     * @param categoryId     L'ID de la catégorie
     * @param organizationId L'ID de l'organisation
     * @return La liste des sous-catégories de véhicule actives pour la catégorie
     */
    List<VehicleSubcategory> findAllActiveByCategoryIdAndOrganizationId(UUID categoryId, UUID organizationId);

    /**
     * Supprime une sous-catégorie de véhicule.
     *
     * @param id L'ID de la sous-catégorie de véhicule à supprimer
     */
    void deleteById(UUID id);
}

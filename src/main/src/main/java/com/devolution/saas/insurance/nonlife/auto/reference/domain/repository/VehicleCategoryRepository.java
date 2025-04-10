package com.devolution.saas.insurance.nonlife.auto.reference.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des catégories de véhicule.
 */
public interface VehicleCategoryRepository {

    /**
     * Sauvegarde une catégorie de véhicule.
     *
     * @param vehicleCategory La catégorie de véhicule à sauvegarder
     * @return La catégorie de véhicule sauvegardée
     */
    VehicleCategory save(VehicleCategory vehicleCategory);

    /**
     * Trouve une catégorie de véhicule par son ID.
     *
     * @param id L'ID de la catégorie de véhicule
     * @return La catégorie de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleCategory> findById(UUID id);

    /**
     * Trouve une catégorie de véhicule par son code.
     *
     * @param code           Le code de la catégorie de véhicule
     * @param organizationId L'ID de l'organisation
     * @return La catégorie de véhicule trouvée, ou empty si non trouvée
     */
    Optional<VehicleCategory> findByCodeAndOrganizationId(String code, UUID organizationId);

    /**
     * Liste toutes les catégories de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des catégories de véhicule
     */
    List<VehicleCategory> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste toutes les catégories de véhicule actives d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des catégories de véhicule actives
     */
    List<VehicleCategory> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Supprime une catégorie de véhicule.
     *
     * @param id L'ID de la catégorie de véhicule à supprimer
     */
    void deleteById(UUID id);
}

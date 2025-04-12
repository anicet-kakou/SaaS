package com.devolution.saas.insurance.nonlife.auto.reference.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleUsage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des types d'usage de véhicule.
 */
public interface VehicleUsageRepository {

    /**
     * Sauvegarde un type d'usage de véhicule.
     *
     * @param vehicleUsage Le type d'usage de véhicule à sauvegarder
     * @return Le type d'usage de véhicule sauvegardé
     */
    VehicleUsage save(VehicleUsage vehicleUsage);

    /**
     * Trouve un type d'usage de véhicule par son ID.
     *
     * @param id L'ID du type d'usage de véhicule
     * @return Le type d'usage de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleUsage> findById(UUID id);

    /**
     * Trouve un type d'usage de véhicule par son code.
     *
     * @param code           Le code du type d'usage de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type d'usage de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleUsage> findByCodeAndOrganizationId(String code, UUID organizationId);

    /**
     * Liste tous les types d'usage de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types d'usage de véhicule
     */
    List<VehicleUsage> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste tous les types d'usage de véhicule actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types d'usage de véhicule actifs
     */
    List<VehicleUsage> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Supprime un type d'usage de véhicule.
     *
     * @param id L'ID du type d'usage de véhicule à supprimer
     */
    void deleteById(UUID id);
}

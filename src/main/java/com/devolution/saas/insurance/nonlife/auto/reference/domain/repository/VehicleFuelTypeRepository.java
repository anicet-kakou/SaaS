package com.devolution.saas.insurance.nonlife.auto.reference.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.VehicleFuelType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des types de carburant de véhicule.
 */
public interface VehicleFuelTypeRepository {

    /**
     * Sauvegarde un type de carburant de véhicule.
     *
     * @param vehicleFuelType Le type de carburant de véhicule à sauvegarder
     * @return Le type de carburant de véhicule sauvegardé
     */
    VehicleFuelType save(VehicleFuelType vehicleFuelType);

    /**
     * Trouve un type de carburant de véhicule par son ID.
     *
     * @param id L'ID du type de carburant de véhicule
     * @return Le type de carburant de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleFuelType> findById(UUID id);

    /**
     * Trouve un type de carburant de véhicule par son code.
     *
     * @param code           Le code du type de carburant de véhicule
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant de véhicule trouvé, ou empty si non trouvé
     */
    Optional<VehicleFuelType> findByCodeAndOrganizationId(String code, UUID organizationId);

    /**
     * Liste tous les types de carburant de véhicule d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant de véhicule
     */
    List<VehicleFuelType> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste tous les types de carburant de véhicule actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant de véhicule actifs
     */
    List<VehicleFuelType> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Supprime un type de carburant de véhicule.
     *
     * @param id L'ID du type de carburant de véhicule à supprimer
     */
    void deleteById(UUID id);
}

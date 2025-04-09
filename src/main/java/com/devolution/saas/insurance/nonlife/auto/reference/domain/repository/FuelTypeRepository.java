package com.devolution.saas.insurance.nonlife.auto.reference.domain.repository;

import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.FuelType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour la gestion des types de carburant.
 */
public interface FuelTypeRepository {

    /**
     * Sauvegarde un type de carburant.
     *
     * @param fuelType Le type de carburant à sauvegarder
     * @return Le type de carburant sauvegardé
     */
    FuelType save(FuelType fuelType);

    /**
     * Trouve un type de carburant par son ID.
     *
     * @param id L'ID du type de carburant
     * @return Le type de carburant trouvé, ou empty si non trouvé
     */
    Optional<FuelType> findById(UUID id);

    /**
     * Trouve un type de carburant par son code.
     *
     * @param code           Le code du type de carburant
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant trouvé, ou empty si non trouvé
     */
    Optional<FuelType> findByCodeAndOrganizationId(String code, UUID organizationId);

    /**
     * Liste tous les types de carburant d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant
     */
    List<FuelType> findAllByOrganizationId(UUID organizationId);

    /**
     * Liste tous les types de carburant actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant actifs
     */
    List<FuelType> findAllActiveByOrganizationId(UUID organizationId);

    /**
     * Supprime un type de carburant.
     *
     * @param id L'ID du type de carburant à supprimer
     */
    void deleteById(UUID id);
}

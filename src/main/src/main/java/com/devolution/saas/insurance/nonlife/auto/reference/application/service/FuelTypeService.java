package com.devolution.saas.insurance.nonlife.auto.reference.application.service;

import com.devolution.saas.insurance.nonlife.auto.reference.application.dto.FuelTypeDTO;
import com.devolution.saas.insurance.nonlife.auto.reference.domain.model.FuelType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service pour la gestion des types de carburant.
 */
public interface FuelTypeService {

    /**
     * Crée un nouveau type de carburant.
     *
     * @param fuelType       Le type de carburant à créer
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant créé
     */
    FuelTypeDTO createFuelType(FuelType fuelType, UUID organizationId);

    /**
     * Met à jour un type de carburant.
     *
     * @param id             L'ID du type de carburant
     * @param fuelType       Le type de carburant mis à jour
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant mis à jour, ou empty si non trouvé
     */
    Optional<FuelTypeDTO> updateFuelType(UUID id, FuelType fuelType, UUID organizationId);

    /**
     * Récupère un type de carburant par son ID.
     *
     * @param id             L'ID du type de carburant
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant trouvé, ou empty si non trouvé
     */
    Optional<FuelTypeDTO> getFuelTypeById(UUID id, UUID organizationId);

    /**
     * Récupère un type de carburant par son code.
     *
     * @param code           Le code du type de carburant
     * @param organizationId L'ID de l'organisation
     * @return Le type de carburant trouvé, ou empty si non trouvé
     */
    Optional<FuelTypeDTO> getFuelTypeByCode(String code, UUID organizationId);

    /**
     * Liste tous les types de carburant d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant
     */
    List<FuelTypeDTO> getAllFuelTypes(UUID organizationId);

    /**
     * Liste tous les types de carburant actifs d'une organisation.
     *
     * @param organizationId L'ID de l'organisation
     * @return La liste des types de carburant actifs
     */
    List<FuelTypeDTO> getAllActiveFuelTypes(UUID organizationId);

    /**
     * Supprime un type de carburant.
     *
     * @param id             L'ID du type de carburant
     * @param organizationId L'ID de l'organisation
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteFuelType(UUID id, UUID organizationId);
}
